package com.github.a_soltysik.animation2d.drawables

import com.github.a_soltysik.animation2d.Animation
import java.awt.Graphics2D

interface Drawable {
    fun update(animation: Animation, frameTime: Double)

    fun render(g2d: Graphics2D)
}