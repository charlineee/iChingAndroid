import SwiftUI

extension Color {
    static let iChingBackground = Color(red: 8/255, green: 7/255, blue: 5/255)
    static let lineTint = Color(red: 227/255, green: 218/255, blue: 201/255)
    static let changingLineTint = Color(red: 122/255, green: 53/255, blue: 53/255)
    static let hexagramSubtext = Color(red: 204/255, green: 204/255, blue: 204/255)
    static let iChingGold = Color(red: 107/255, green: 96/255, blue: 85/255)
}

struct AppTheme {
    static func backgroundColor(isDark: Bool) -> Color {
        return isDark ? .iChingBackground : .lineTint
    }
    
    static func textColor(isDark: Bool) -> Color {
        return isDark ? .hexagramSubtext : .iChingBackground
    }
    
    static func primaryColor() -> Color {
        return .changingLineTint
    }
    
    static func secondaryColor() -> Color {
        return .iChingGold
    }
}

extension View {
    func vignette(isDark: Bool) -> some View {
        let color = isDark ? Color.lineTint.opacity(0.1) : Color.black.opacity(0.1)
        return self.background(
            RadialGradient(
                gradient: Gradient(colors: [.clear, color]),
                center: .center,
                startRadius: 0,
                endRadius: 500
            )
        )
    }
}
