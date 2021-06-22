package com.github.a_soltysik.animation2d.gui

import com.github.a_soltysik.animation2d.Animation
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JLayeredPane
import javax.swing.JPanel

class AnimationPanel(animation: Animation, preferredSize: Dimension) : JLayeredPane(){
    val framePanel: FramePanel = FramePanel(animation)
    val loggerPanel: LoggerPanel = LoggerPanel()
    val optionsPanel: OptionsPanel = OptionsPanel()
    val top = TripleSplitPane()

    companion object {
        private const val BOTTOM = 0
        private const val MIDDLE = 1
        private const val TOP = 2
    }

    init {
        add(framePanel, MIDDLE as Integer)
        add(top, TOP as Integer)

        framePanel.background = Color.pink

        val transparentPanel = JPanel()
        transparentPanel.layout = BoxLayout(transparentPanel, BoxLayout.X_AXIS)
        transparentPanel.isOpaque = false
        top.setAllOpaque(false)
        top.leftComponent = loggerPanel
        top.centerComponent = transparentPanel
        top.rightComponent = optionsPanel

        top.hideRight()
        top.hideLeft()

        framePanel.isFocusable = false
        loggerPanel.isFocusable = false
        optionsPanel.isFocusable = false

        this.preferredSize = preferredSize
        animation.animationPanel = this
        animation.logger = loggerPanel.logger
        animation.options = optionsPanel

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                framePanel.size = size
                top.size = size
            }
        })

        addKeyListener(object : KeyListener {
            var loggerOpened = false
            var optionsOpened = false
            override fun keyTyped(e: KeyEvent?) {}

            override fun keyPressed(e: KeyEvent?) {
                if (e != null) {
                    if (e.keyCode == KeyEvent.VK_L) {
                        loggerOpened = if (loggerOpened) {
                            this@AnimationPanel.top.hideLeft()
                            false
                        } else {
                            this@AnimationPanel.top.openLeft()
                            true
                        }
                    }
                    if (e.keyCode == KeyEvent.VK_O) {
                        println("aa")
                        optionsOpened = if (optionsOpened) {
                            this@AnimationPanel.top.hideRight()
                            false
                        } else {
                            this@AnimationPanel.top.openRight()
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