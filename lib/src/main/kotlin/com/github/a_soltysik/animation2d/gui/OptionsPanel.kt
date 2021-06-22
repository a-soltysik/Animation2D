package com.github.a_soltysik.animation2d.gui

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionListener
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.util.*
import javax.swing.*
import javax.swing.event.ChangeListener
import kotlin.collections.mapOf
import kotlin.math.roundToInt

class OptionsPanel : JRootPane() {

    private val intSliders: MutableMap<IntParameter, JSlider> = mutableMapOf()
    private val floatSliders: MutableMap<FloatParameter, JSlider> = mutableMapOf()
    private val slidersPanel = JPanel()


    init {
        slidersPanel.background = Color.gray
        slidersPanel.layout = BoxLayout(slidersPanel, BoxLayout.Y_AXIS)
        slidersPanel.border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        layout = BorderLayout()
        val menuBar = JMenuBar()
        val menu = JMenu("Menu")
        menuBar.add(menu)
        add(BorderLayout.NORTH, menuBar)
        add(BorderLayout.CENTER, slidersPanel)

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                println("ee $isVisible")
                menuBar.size = Dimension(size.width, menu.preferredSize.height)
                slidersPanel.size = Dimension(size.width, size.height - menu.height)
            }
        })

    }



    fun addIntParameter(parameter: IntParameter) {
        val slider = IntSlider(parameter)
        intSliders[parameter] = slider
        slidersPanel.add(slider)
        slidersPanel.add(Box.createVerticalStrut(15))
        /*intSliders.computeIfAbsent(parameter) { p ->
            val slider = IntSlider(p)
            println(slider)
            slidersPanel.add(slider)
            return@computeIfAbsent slider
        }*/
    }

    fun addFloatParameter(parameter: FloatParameter) {
        floatSliders.computeIfAbsent(parameter) { p ->
           val slider = FloatSlider(p)
           slidersPanel.add(slider)
           return@computeIfAbsent slider
        }
    }

    fun addChangeListener(parameter: IntParameter, listener: ChangeListener) =
        intSliders[parameter]?.addChangeListener(listener)

    fun addChangeListener(parameter: FloatParameter, listener: ChangeListener) =
        floatSliders[parameter]?.addChangeListener(listener)

    fun removeChangeListener(parameter: IntParameter, listener: ChangeListener) =
        intSliders[parameter]?.removeChangeListener(listener)

    fun removeChangeListener(parameter: FloatParameter, listener: ChangeListener) =
        floatSliders[parameter]?.removeChangeListener(listener)


    private inner class IntSlider(private val parameter: IntParameter) : JSlider() {
        init {
            isFocusable = false
            minimum = parameter.min
            maximum = parameter.max / parameter.tick
            value = parameter.value / parameter.tick
            setCustomLabels()
            paintLabels = true
            addChangeListener {
                parameter.value = value * parameter.tick
                setCustomLabels()
            }
        }

        private fun setCustomLabels() {
            labelTable = Hashtable(
                mapOf(
                    minimum to JLabel(parameter.min.toString()),
                    (minimum + maximum) / 2 to JLabel(parameter.name + ": " + parameter.value),
                    maximum to JLabel(parameter.max.toString())
                )
            )
        }
    }

    private class FloatSlider(private val parameter: FloatParameter) : JSlider() {
        init {
            isFocusable = false
            val scale = 1 / parameter.tick
            minimum = (parameter.min * scale).roundToInt()
            maximum = (parameter.max * scale).roundToInt()
            value = (parameter.value * scale).roundToInt()
            setCustomLabels()
            paintLabels = true
            addChangeListener {
                parameter.value = value / scale
                setCustomLabels()
            }
        }

        private fun setCustomLabels() {
            labelTable = Hashtable(
                mapOf(
                    minimum to JLabel(parameter.min.toString()),
                    (minimum + maximum) / 2 to JLabel(parameter.name + ": " + parameter.value),
                    maximum to JLabel(parameter.max.toString())
                )
            )
        }
    }
}
