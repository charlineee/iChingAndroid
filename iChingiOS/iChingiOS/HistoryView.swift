import SwiftUI
import SharedKit

struct HistoryView: View {
    @ObservedObject var viewModel: IChingViewModel
    let isDarkMode: Bool
    var onReadingClick: (ReadingEntity) -> Void
    var onBack: () -> Void

    var body: some View {
        let backgroundColor = AppTheme.backgroundColor(isDark: isDarkMode)
        let textColor = AppTheme.textColor(isDark: isDarkMode)
        
        VStack(spacing: 0) {
            // Header
            HStack {
                Button(action: onBack) {
                    Image(systemName: "chevron.left")
                        .foregroundColor(textColor)
                }
                Spacer()
                Text("HISTORY")
                    .font(.custom("ChakraPetch-SemiBold", size: 20))
                    .kerning(2)
                    .foregroundColor(AppTheme.secondaryColor())
                Spacer()
                Button(action: { viewModel.clearHistory() }) {
                    Text("CLEAR")
                        .font(.custom("ChakraPetch-Regular", size: 12))
                        .foregroundColor(.red)
                }
                .opacity(viewModel.savedReadings.isEmpty ? 0 : 1)
            }
            .padding()
            .background(backgroundColor)

            Divider().background(AppTheme.secondaryColor().opacity(0.5))

            if viewModel.savedReadings.isEmpty {
                Spacer()
                VStack(spacing: 16) {
                    Image(systemName: "tray.fill")
                        .font(.system(size: 60))
                        .foregroundColor(.gray.opacity(0.3))
                    Text("No saved readings yet")
                        .font(.custom("ChakraPetch-Light", size: 16))
                        .foregroundColor(.gray)
                }
                Spacer()
            } else {
                List {
                    ForEach(viewModel.savedReadings, id: \.id) { reading in
                        HistoryRow(reading: reading, textColor: textColor)
                            .contentShape(Rectangle())
                            .onTapGesture {
                                onReadingClick(reading)
                            }
                            .listRowBackground(Color.clear)
                            .listRowInsets(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
                    }
                    .onDelete { indexSet in
                        indexSet.forEach { index in
                            let reading = viewModel.savedReadings[index]
                            viewModel.deleteReading(reading: reading)
                        }
                    }
                }
                .listStyle(.plain)
            }
        }
        .background(backgroundColor.vignette(isDark: isDarkMode).ignoresSafeArea())
    }
}

struct HistoryRow: View {
    let reading: ReadingEntity
    let textColor: Color
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(reading.question.isEmpty ? "Untitled Question" : reading.question)
                .font(.custom("ChakraPetch-SemiBold", size: 16))
                .foregroundColor(textColor)
                .lineLimit(1)
            
            HStack {
                Text(formatDate(timestamp: reading.timestamp))
                Text("•")
                Text("Hexagram \(reading.primaryHexNumber)")
            }
            .font(.custom("ChakraPetch-Light", size: 12))
            .foregroundColor(textColor.opacity(0.6))
        }
        .padding(.vertical, 8)
    }
    
    private func formatDate(timestamp: Int64) -> String {
        let date = Date(timeIntervalSince1970: Double(timestamp) / 1000)
        let formatter = RelativeDateTimeFormatter()
        formatter.unitsStyle = .full
        return formatter.localizedString(for: date, relativeTo: Date())
    }
}
