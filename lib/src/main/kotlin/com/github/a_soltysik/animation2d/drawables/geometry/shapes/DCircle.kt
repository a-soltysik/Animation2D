package com.github.a_soltysik.animation2d.drawables.geometry.shapes

import com.github.a_soltysik.animation2d.math.Rectangle
import com.github.a_soltysik.animation2d.math.Vector2
import java.awt.geom.Ellipse2D

class DCircle(private val radius: Float) : IShape {
    private var center = Vector2.ZERO
    override fun toAWTShape() = Ellipse2D.Float(center.x, center.y, radius * 2, radius * 2)

    override fun translate(diff: Vector2) {
        center += diff
    }

    override fun rotate(center: Vector2, angleRad: Float) {}

    override fun getBoundingBox() =
        Rectangle(
            Vector2(center.x - radius, center.y - radius),
            Vector2(center.x + radius, center.y + radius)
        )
}