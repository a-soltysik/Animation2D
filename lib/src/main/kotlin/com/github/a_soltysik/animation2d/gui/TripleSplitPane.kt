package com.github.a_soltysik.animation2d.gui

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSplitPane

open class TripleSplitPane : JPanel() {

    private val leftSplitPane = JSplitPane()
    private val rightSplitPane = JSplitPane()

    var leftComponent: JComponent? = null
        set(value) {
            leftSplitPane.leftComponent = value
            field = value
        }
    var centerComponent: JComponent? = null
        set(value) {
            leftSplitPane.rightComponent = value
            field = value
        }
    var rightComponent: JComponent? = null
        set(value) {
            rightSplitPane.rightComponent = value
            field = value
        }
    var divisorSize: Int = rightSplitPane.dividerSize
    set(value) {
        rightSplitPane.dividerSize = value
        leftSplitPane.dividerSize = value
        field = value
    }

    fun setAllOpaque(isOpaque: Boolean) {
        setOpaque(isOpaque)
        leftSplitPane.isOpaque = isOpaque
        rightSplitPane.isOpaque = isOpaque
    }

    fun hideLeft() {
        leftSplitPane.dividerLocation = 0
        leftComponent?.size = Dimension(0, height)
    }

    fun openLeft() {
        leftSplitPane.dividerLocation = leftSplitPane.lastDividerLocation
        leftComponent?.size = Dimension(width / 3, height)
    }

    fun hideRight() {
        rightSplitPane.dividerLocation = rightSplitPane.width
        rightComponent?.size = Dimension(0, height)
    }

    fun openRight() {
        rightSplitPane.dividerLocation = rightSplitPane.lastDividerLocation
        rightComponent?.size = Dimension(width / 3, height)
    }

    init {
        layout = BorderLayout()
        rightSplitPane.leftComponent = leftSplitPane
        add(BorderLayout.CENTER, rightSplitPane)
        leftSplitPane.isOneTouchExpandable = true
        leftSplitPane.dividerLocation = leftSplitPane.width / 2
        rightSplitPane.isOneTouchExpandable = true
        rightSplitPane.dividerLocation = rightSplitPane.width * 2 / 3
        leftComponent?.size = Dimension(width / 3, height)
        centerComponent?.size = Dimension(width / 3, height)
        rightComponent?.size = Dimension(width / 3, height)

        divisorSize = 10

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                centerComponent?.size = size
                rightSplitPane.size = size
            }
        })
    }
}