package com.github.a_soltysik.animation2d.drawables.geometry.debug

import com.github.a_soltysik.animation2d.Animation
import com.github.a_soltysik.animation2d.drawables.Drawable
import com.github.a_soltysik.animation2d.math.Vector2
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Line2D

class LogSegment(private val start: Vector2, private val end: Vector2, private val color: Color = Color.green) : Drawable{
    override fun update(animation: Animation, frameTime: Double) {}

    override fun render(g2d: Graphics2D) {
        g2d.color = color
        g2d.draw(
            Line2D.Float(
                start.x, start.y,
                end.x, end.y
            )
        )
    }
}