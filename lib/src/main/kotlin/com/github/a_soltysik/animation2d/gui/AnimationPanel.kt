package com.github.a_soltysik.animation2d.gui

import com.github.a_soltysik.animation2d.Animation
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JLayeredPane

class AnimationPanel(animation: Animation, preferredSize: Dimension) : JLayeredPane(){
    val framePanel: FramePanel = FramePanel(animation)
    init {
        add(framePanel, 1)
        this.preferredSize = preferredSize
        animation.animationPanel = this
        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                framePanel.size = size;
            }
        })
    }
}