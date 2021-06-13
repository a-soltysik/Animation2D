package com.github.a_soltysik.animation2d.gui

import com.github.a_soltysik.animation2d.Animation
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JLayeredPane

class AnimationPanel(animation: Animation, preferredSize: Dimension) : JLayeredPane(){
    val framePanel: FramePanel = FramePanel(animation)
    val loggerPanel = LoggerPanel()
    companion object {
        private const val BOTTOM = 0
        private const val MIDDLE = 1
        private const val TOP = 2
    }
    init {
        add(framePanel, MIDDLE as Integer)
        add(loggerPanel, BOTTOM as Integer)

        framePanel.isFocusable = false;
        loggerPanel.isFocusable = false;

        this.preferredSize = preferredSize
        animation.animationPanel = this
        animation.logger = loggerPanel.logger

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                framePanel.size = size;
                loggerPanel.size = Dimension(size.width / 2, size.height)
            }
        })

        addKeyListener(object : KeyListener {
            var loggerOpened = false
            override fun keyTyped(e: KeyEvent?) {}

            override fun keyPressed(e: KeyEvent?) {
                if (e != null) {
                    if (e.keyCode == KeyEvent.VK_L) {
                        loggerOpened = if (loggerOpened) {
                            this@AnimationPanel.setLayer(loggerPanel, BOTTOM)
                            false
                        } else {
                            this@AnimationPanel.setLayer(loggerPanel, TOP)
                            true
                        }
                    }
                }
            }
            override fun keyReleased(e: KeyEvent?) {}
        })
    }

    override fun addNotify() {
        super.addNotify()
        requestFocus()
    }
}