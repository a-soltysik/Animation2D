package com.github.a_soltysik.animation2d.gui

import com.github.a_soltysik.animation2d.Animation
import javax.swing.JLayeredPane

class AnimationPanel(animation: Animation) : JLayeredPane() {
    val framePanel: FramePanel = FramePanel(animation)
    init {
        add(framePanel, 0)
        animation.animationPanel = this
    }
}