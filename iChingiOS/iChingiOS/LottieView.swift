import SwiftUI
import Lottie

struct LottieView: UIViewRepresentable {
    let name: String
    let loopMode: LottieLoopMode
    let isPlaying: Bool
    let speed: CGFloat
    
    init(name: String, loopMode: LottieLoopMode = .loop, isPlaying: Bool = true, speed: CGFloat = 1.0) {
        self.name = name
        self.loopMode = loopMode
        self.isPlaying = isPlaying
        self.speed = speed
    }
    
    func makeUIView(context: Context) -> UIView {
        let view = UIView(frame: .zero)
        
        let animationView = LottieAnimationView(name: name)
        animationView.loopMode = loopMode
        animationView.animationSpeed = speed
        animationView.contentMode = .scaleAspectFit
        animationView.translatesAutoresizingMaskIntoConstraints = false
        
        view.addSubview(animationView)
        
        NSLayoutConstraint.activate([
            animationView.heightAnchor.constraint(equalTo: view.heightAnchor),
            animationView.widthAnchor.constraint(equalTo: view.widthAnchor),
            animationView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            animationView.centerYAnchor.constraint(equalTo: view.centerYAnchor)
        ])
        
        return view
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {
        if let animationView = uiView.subviews.first as? LottieAnimationView {
            animationView.animationSpeed = speed
            if isPlaying {
                if !animationView.isAnimationPlaying {
                    animationView.play()
                }
            } else {
                animationView.stop()
            }
        }
    }
}
