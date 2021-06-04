package com.github.a_soltysik.animation2d.drawables.geometry.debug

import com.github.a_soltysik.animation2d.Animation
import com.github.a_soltysik.animation2d.drawables.Drawable
import com.github.a_soltysik.animation2d.math.Rectangle
import com.github.a_soltysik.animation2d.math.Vector2
import java.awt.Color
import java.awt.Graphics2D

class LogRectangle (rectangle: Rectangle, color: Color = Color.green): Drawable {

    private val lines = arrayOf(
        LogSegment(rectangle.min, Vector2(rectangle.max.x, rectangle.min.y), color),
        LogSegment(Vector2(rectangle.max.x, rectangle.min.y), rectangle.max, color),
        LogSegment(rectangle.max, Vector2(rectangle.min.x, rectangle.max.y), color),
        LogSegment(Vector2(rectangle.min.x, rectangle.max.y), rectangle.min, color)
    )
    override fun update(animation: Animation, frameTime: Double) {}

    override fun render(g2d: Graphics2D) {
        for (line in lines) {
            line.render(g2d)
        }
    }
}