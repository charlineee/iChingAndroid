import SwiftUI
import SharedKit

struct ResultView: View {
    let reading: ReadingResult
    let isSaved: Bool
    let isDarkMode: Bool
    var onBack: () -> Void
    var onSave: () -> Void

    var body: some View {
        let backgroundColor = AppTheme.backgroundColor(isDark: isDarkMode)
        let textColor = AppTheme.textColor(isDark: isDarkMode)
        
        ScrollView {
            VStack(alignment: .center, spacing: 24) {
                // Header
                VStack(spacing: 8) {
                    Text("Hexagram \(reading.primaryHex.hex): \(reading.primaryName)")
                        .font(.custom("ChakraPetch-SemiBold", size: 22))
                        .foregroundColor(textColor)
                    
                    Text(reading.primaryHex.tradChinese)
                        .font(.custom("ChakraPetch-Regular", size: 18))
                        .foregroundColor(textColor.opacity(0.7))
                }
                .padding(.top, 20)

                Divider().background(AppTheme.secondaryColor())

                // Judgment
                SectionView(title: "THE JUDGMENT", content: reading.judgment, textColor: textColor)

                // Image
                SectionView(title: "THE IMAGE", content: reading.image, textColor: textColor)

                // Changing Lines
                if !reading.changingLines.isEmpty {
                    Divider().background(AppTheme.secondaryColor())
                    Text("CHANGING LINES")
                        .font(.custom("ChakraPetch-SemiBold", size: 14))
                        .kerning(4)
                        .foregroundColor(AppTheme.secondaryColor())
                    
                    ForEach(reading.changingLines.keys.sorted(), id: \.self) { lineNum in
                        VStack(alignment: .leading, spacing: 8) {
                            Text("Line \(lineNum)")
                                .font(.custom("ChakraPetch-SemiBold", size: 14))
                                .foregroundColor(textColor)
                            Text(reading.changingLines[lineNum] ?? "")
                                .font(.custom("ChakraPetch-Light", size: 16))
                                .foregroundColor(textColor)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                }

                // Relating Hexagram
                if let relatingHex = reading.relatingHex {
                    Divider().background(AppTheme.secondaryColor())
                    Text("CHANGING TO")
                        .font(.custom("ChakraPetch-SemiBold", size: 14))
                        .kerning(4)
                        .foregroundColor(AppTheme.secondaryColor())
                    
                    VStack(spacing: 8) {
                        Text("Hexagram \(relatingHex.hex): \(reading.relatingName ?? "")")
                            .font(.custom("ChakraPetch-SemiBold", size: 18))
                            .foregroundColor(textColor)
                        Text(relatingHex.tradChinese)
                            .font(.custom("ChakraPetch-Regular", size: 16))
                            .foregroundColor(textColor.opacity(0.7))
                    }

                    SectionView(title: "THE JUDGMENT", content: relatingHex.judgment.text, textColor: textColor)
                    SectionView(title: "THE IMAGE", content: relatingHex.image.text, textColor: textColor)
                }

                Spacer(minLength: 32)

                Button(action: onSave) {
                    HStack {
                        Image(systemName: isSaved ? "checkmark.circle.fill" : "square.and.arrow.down")
                        Text(isSaved ? "SAVED" : "SAVE READING")
                    }
                    .font(.custom("ChakraPetch-SemiBold", size: 16))
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(isSaved ? Color.green : AppTheme.primaryColor())
                    .cornerRadius(10)
                }
                .disabled(isSaved)

                Spacer(minLength: 40)
            }
            .padding(.horizontal, 24)
        }
        .background(backgroundColor.vignette(isDark: isDarkMode).ignoresSafeArea())
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: onBack) {
                    Image(systemName: "chevron.left")
                        .foregroundColor(textColor)
                    Text("CAST AGAIN")
                        .font(.custom("ChakraPetch-SemiBold", size: 14))
                        .foregroundColor(textColor)
                }
            }
        }
    }
}

// Helper struct to match Android's ReadingResult
struct ReadingResult {
    let primaryHex: HexagramData
    let primaryName: String
    let relatingHex: HexagramData?
    let relatingName: String?
    let changingLines: [Int32: String]
    let judgment: String
    let image: String
}

struct SectionView: View {
    let title: String
    let content: String
    let textColor: Color

    var body: some View {
        VStack(alignment: .center, spacing: 8) {
            Text(title)
                .font(.custom("ChakraPetch-SemiBold", size: 14))
                .kerning(4)
                .foregroundColor(AppTheme.secondaryColor())
            Text(content)
                .font(.custom("ChakraPetch-Light", size: 16))
                .foregroundColor(textColor)
                .multilineTextAlignment(.center)
        }
    }
}
