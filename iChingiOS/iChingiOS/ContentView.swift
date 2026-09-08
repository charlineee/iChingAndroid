import SwiftUI
import SharedKit
import Lottie

struct ContentView: View {
    @StateObject private var viewModel = IChingViewModel()
    @State private var showResult = false
    @State private var showHistory = false
    
    @AppStorage("isDarkMode") private var isDarkMode = true

    var body: some View {
        NavigationStack {
            ZStack {
                // Background
                AppTheme.backgroundColor(isDark: isDarkMode).ignoresSafeArea()
                
                // Vignette overlay
                Color.clear.vignette(isDark: isDarkMode).ignoresSafeArea()

                VStack(spacing: 0) {
                    // Header
                    HStack {
                        Button(action: { 
                            viewModel.refreshHistory()
                            showHistory = true 
                        }) {
                            Image(systemName: "clock.arrow.circlepath")
                                .font(.title3)
                                .foregroundColor(AppTheme.secondaryColor())
                        }
                        Spacer()
                        Text("易經")
                            .font(.custom("ChakraPetch-SemiBold", size: 36))
                            .foregroundColor(AppTheme.secondaryColor())
                        Spacer()
                        Color.clear.frame(width: 32, height: 32)
                    }
                    .padding(.horizontal)
                    .padding(.top, 10)

                    Text("BOOK OF CHANGES")
                        .font(.custom("ChakraPetch-Light", size: 18))
                        .kerning(4)
                        .foregroundColor(AppTheme.textColor(isDark: isDarkMode))
                        .padding(.bottom, 10)

                    Spacer(minLength: 5)

                    // Hexagram Lines
                    VStack(spacing: 6) {
                        if viewModel.coinThrows.isEmpty && !viewModel.isComplete {
                            Text("Cast to see lines")
                                .font(.custom("ChakraPetch-Light", size: 14))
                                .foregroundColor(.gray)
                        } else {
                            ForEach(Array(viewModel.coinThrows.enumerated()).reversed(), id: \.offset) { index, t in
                                LineView(lineType: t.lineType, drawableVariant: Int(t.drawableVariant), isComplete: viewModel.isComplete, isDarkMode: isDarkMode)
                            }
                        }
                    }
                    .frame(height: 150)
                    .frame(maxWidth: .infinity)

                    Spacer(minLength: 5)

                    // Coins
                    HStack(spacing: 15) {
                        ForEach(0..<3, id: \.self) { i in
                            CoinView(
                                isFlipping: viewModel.isFlipping,
                                result: viewModel.coinThrows.last?.coins[i],
                                isDark: isDarkMode
                            )
                        }
                    }
                    .frame(height: 110)

                    Spacer(minLength: 5)

                    TextField("Your question", text: $viewModel.question)
                        .textFieldStyle(.roundedBorder)
                        .padding(.horizontal)

                    Spacer(minLength: 15)

                    VStack(spacing: 10) {
                        Button(action: {
                            if viewModel.isComplete {
                                viewModel.reset()
                            } else {
                                viewModel.throwCoins()
                            }
                        }) {
                            Text(viewModel.isComplete ? "CAST AGAIN" : "THROW COINS")
                                .font(.custom("ChakraPetch-SemiBold", size: 16))
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 14)
                                .background(AppTheme.secondaryColor())
                                .cornerRadius(10)
                        }
                        .disabled(viewModel.isFlipping)

                        if viewModel.isComplete {
                            Button(action: { 
                                viewModel.buildResult(hex: viewModel.hexagram!)
                                showResult = true 
                            }) {
                                Text("READ MORE")
                                    .font(.custom("ChakraPetch-SemiBold", size: 16))
                                    .foregroundColor(AppTheme.textColor(isDark: isDarkMode))
                                    .frame(maxWidth: .infinity)
                                    .padding(.vertical, 14)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 10)
                                            .stroke(AppTheme.primaryColor(), lineWidth: 2)
                                    )
                            }
                        }
                    }
                    .padding(.horizontal)

                    Spacer(minLength: 5)

                    if let hex = viewModel.hexagram {
                        VStack(spacing: 4) {
                            Text("Hexagram \(hex.primaryNumber): \(hex.primaryName)")
                                .font(.custom("ChakraPetch-SemiBold", size: 16))
                                .foregroundColor(AppTheme.textColor(isDark: isDarkMode))

                            if hex.hasChangingLines {
                                Text("→ \(hex.relatingNumber?.intValue.description ?? ""): \(hex.relatingName ?? "")")
                                    .font(.custom("ChakraPetch-Regular", size: 13))
                                    .foregroundColor(AppTheme.textColor(isDark: isDarkMode).opacity(0.7))
                            }
                        }
                    } else {
                        VStack(spacing: 4) {
                            Text(" ").font(.custom("ChakraPetch-SemiBold", size: 16))
                            Text(" ").font(.custom("ChakraPetch-Regular", size: 13))
                        }
                    }

                    Spacer(minLength: 10)
                    
                    // Theme Toggle
                    Button(action: { isDarkMode.toggle() }) {
                        Image(systemName: isDarkMode ? "sun.max.fill" : "moon.fill")
                            .font(.title2)
                            .foregroundColor(AppTheme.textColor(isDark: isDarkMode))
                            .padding(12)
                            .background(Circle().fill(AppTheme.textColor(isDark: isDarkMode).opacity(0.1)))
                    }
                    .padding(.bottom, 10)
                }
            }
            .navigationDestination(isPresented: $showResult) {
                if let reading = viewModel.currentReading {
                    ResultView(
                        reading: reading,
                        isSaved: viewModel.isSaved,
                        isDarkMode: isDarkMode,
                        onBack: { 
                            showResult = false
                            viewModel.reset()
                        },
                        onSave: { viewModel.saveReading() }
                    )
                }
            }
            .sheet(isPresented: $showHistory) {
                HistoryView(
                    viewModel: viewModel,
                    isDarkMode: isDarkMode,
                    onReadingClick: { reading in
                        viewModel.loadHistoryReading(reading: reading)
                        showHistory = false
                        showResult = true
                    },
                    onBack: { showHistory = false }
                )
            }
            .preferredColorScheme(isDarkMode ? .dark : .light)
            .toolbar(.hidden)
        }
    }
}

struct CoinView: View {
    let isFlipping: Bool
    let result: String?
    let isDark: Bool
    
    @State private var offsetY: CGFloat = 0
    
    var body: some View {
        ZStack {
            if isFlipping {
                LottieView(
                    name: isDark ? "catflipcoin2x" : "feathercoin2x",
                    loopMode: .loop,
                    isPlaying: true,
                    speed: 4.0
                )
                .frame(width: 110, height: 110)
            } else {
                let imageName = getResultImageName()
                Image(imageName)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 80, height: 80)
            }
        }
        .frame(width: 110, height: 110)
        .offset(y: offsetY)
        .onChange(of: isFlipping) { flipping in
            if flipping {
                withAnimation(.easeOut(duration: 0.25)) {
                    offsetY = -200
                }
                
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
                    withAnimation(.easeIn(duration: 0.35)) {
                        offsetY = 0
                    }
                }
            } else {
                offsetY = 0
            }
        }
    }
    
    private func getResultImageName() -> String {
        if isDark {
            return result == "tails" ? "tail5" : "heads1"
        } else {
            return result == "tails" ? "feather6" : "egg1"
        }
    }
}

struct LineView: View {
    let lineType: LineType
    let drawableVariant: Int
    let isComplete: Bool
    let isDarkMode: Bool

    var body: some View {
        let imageName = getLineImageName()
        Image(imageName)
            .resizable()
            .renderingMode(.template)
            .scaledToFit()
            .foregroundColor(tint)
            .frame(width: 180, height: 20)
    }

    private func getLineImageName() -> String {
        if lineType == .yang || lineType == .yangChanging {
            return drawableVariant == 0 ? "yangline1" : "yangline2"
        } else {
            return drawableVariant == 0 ? "yinline1" : "yinline2"
        }
    }

    private var tint: Color {
        if isComplete && lineType.isChanging() {
            return AppTheme.primaryColor()
        }
        return AppTheme.textColor(isDark: isDarkMode)
    }
}

#Preview {
    ContentView()
}
