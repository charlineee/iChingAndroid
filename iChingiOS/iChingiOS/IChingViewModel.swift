import Foundation
import SharedKit
import Combine

@MainActor
class IChingViewModel: ObservableObject {
    @Published var coinThrows: [CoinThrow] = []
    @Published var hexagram: Hexagram? = nil
    @Published var isComplete: Bool = false
    @Published var isFlipping: Bool = false
    @Published var question: String = ""
    @Published var isSaved: Bool = false
    @Published var currentReading: ReadingResult? = nil
    
    // History
    @Published var savedReadings: [ReadingEntity] = []

    private let repository: IChingRepository

    init() {
        // Initialize Database
        let builder = AppDatabaseKt.getDatabaseBuilder()
        let database = builder.build()

        // Load JSON from Bundle
        let jsonString: String
        if let path = Bundle.main.path(forResource: "iching_wilhelm_translation", ofType: "json"),
           let content = try? String(contentsOfFile: path) {
            jsonString = content
        } else {
            jsonString = "{}"
            print("Warning: iching_wilhelm_translation.json not found in bundle")
        }

        self.repository = IChingRepository(readingDao: database.readingDao(), jsonString: jsonString)
        
        refreshHistory()
    }

    func refreshHistory() {
        Task {
            do {
                let history = try await repository.fetchHistory()
                self.savedReadings = history as! [ReadingEntity]
            } catch {
                print("Failed to fetch history: \(error)")
            }
        }
    }

    func throwCoins() {
        guard !isComplete else { return }

        isFlipping = true

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.6) {
            self.isFlipping = false

            let coins = (0..<3).map { _ in Bool.random() ? "heads" : "tails" }
            let sum = coins.reduce(0) { $0 + ($1 == "heads" ? 3 : 2) }

            let lineType: LineType
            switch sum {
            case 6: lineType = .yinChanging
            case 7: lineType = .yang
            case 8: lineType = .yin
            default: lineType = .yangChanging
            }

            let newThrow = CoinThrow(coins: coins, sum: Int32(sum), lineType: lineType, drawableVariant: Int32.random(in: 0...1))
            self.coinThrows.append(newThrow)

            if self.coinThrows.count == 6 {
                self.isComplete = true
                let hex = HexagramCalculator.shared.buildHexagram(coinThrows: self.coinThrows)
                self.hexagram = hex
                self.buildResult(hex: hex)
            }
        }
    }

    func buildResult(hex: Hexagram) {
        let primary = repository.getHexagram(number: hex.primaryNumber)
        
        var relating: HexagramData? = nil
        if let relNum = hex.relatingNumber {
            relating = repository.getHexagram(number: relNum.int32Value)
        }
        
        let changingLineNumbers = self.coinThrows.enumerated().compactMap { (index, t) -> Int32? in
            return t.lineType.isChanging() ? Int32(index + 1) : nil
        }
        
        let changingLineNumbersKmp = changingLineNumbers.map { KotlinInt(value: $0) }
        
        let changingLinesKmp = repository.getChangingLineTexts(hexNumber: hex.primaryNumber, lineNumbers: changingLineNumbersKmp)
        var changingLinesSwift: [Int32: String] = [:]
        for (key, value) in changingLinesKmp {
            if let kInt = key as? KotlinInt {
                changingLinesSwift[kInt.int32Value] = value as? String ?? ""
            }
        }

        if let primary = primary {
            self.currentReading = ReadingResult(
                primaryHex: primary,
                primaryName: hex.primaryName,
                relatingHex: relating,
                relatingName: hex.relatingName,
                changingLines: changingLinesSwift,
                judgment: primary.judgment.text,
                image: primary.image.text
            )
        }
    }
    
    func loadHistoryReading(reading: ReadingEntity) {
        let primaryResult = repository.getHexagram(number: Int32(reading.primaryHexNumber))
        
        var relatingResult: HexagramData? = nil
        if let relNum = reading.relatingHexNumber {
            relatingResult = repository.getHexagram(number: relNum.int32Value)
        }
        
        let changingLinesKmp = repository.getChangingLineTexts(hexNumber: Int32(reading.primaryHexNumber), lineNumbers: reading.changingLineNumbers)
        var changingLinesSwift: [Int32: String] = [:]
        for (key, value) in changingLinesKmp {
            if let kInt = key as? KotlinInt {
                changingLinesSwift[kInt.int32Value] = value as? String ?? ""
            }
        }
        
        if let primary = primaryResult {
            let primaryName = (HexagramCalculator.shared.HEXAGRAM_NAMES[KotlinInt(value: primary.hex)] as? String) ?? primary.english
            
            var relatingName: String? = nil
            if let rel = relatingResult {
                relatingName = (HexagramCalculator.shared.HEXAGRAM_NAMES[KotlinInt(value: rel.hex)] as? String) ?? rel.english
            }

            self.currentReading = ReadingResult(
                primaryHex: primary,
                primaryName: primaryName,
                relatingHex: relatingResult,
                relatingName: relatingName,
                changingLines: changingLinesSwift,
                judgment: primary.judgment.text,
                image: primary.image.text
            )
            self.question = reading.question
            self.isSaved = true
            self.isComplete = true
        }
    }

    func saveReading() {
        guard let reading = currentReading else { return }
        
        let changingLineNumbers = self.coinThrows.enumerated().compactMap { (index, t) -> Int32? in
            return t.lineType.isChanging() ? Int32(index + 1) : nil
        }

        let entity = ReadingEntity(
            id: 0,
            question: self.question,
            timestamp: Int64(Date().timeIntervalSince1970 * 1000),
            primaryHexNumber: reading.primaryHex.hex,
            relatingHexNumber: reading.relatingHex.map { KotlinInt(value: $0.hex) },
            changingLineNumbers: changingLineNumbers.map { KotlinInt(value: $0) }
        )

        Task {
            do {
                try await repository.saveReading(reading: entity)
                self.isSaved = true
                refreshHistory()
            } catch {
                print("Failed to save reading: \(error)")
            }
        }
    }
    
    func deleteReading(reading: ReadingEntity) {
        Task {
            do {
                try await repository.deleteReading(reading: reading)
                refreshHistory()
            } catch {
                print("Failed to delete reading: \(error)")
            }
        }
    }
    
    func clearHistory() {
        Task {
            do {
                try await repository.clearHistory()
                refreshHistory()
            } catch {
                print("Failed to clear history: \(error)")
            }
        }
    }

    func reset() {
        self.coinThrows = []
        self.hexagram = nil
        self.isComplete = false
        self.question = ""
        self.isSaved = false
        self.currentReading = nil
    }
}
