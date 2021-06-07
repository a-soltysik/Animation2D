package com.github.a_soltysik.animation2d.drawables

import com.github.a_soltysik.animation2d.Animation
import com.github.a_soltysik.animation2d.drawables.geometry.shapes.IShape
import com.github.a_soltysik.animation2d.math.Vector2
import java.awt.Color
import java.awt.Graphics2D

abstract class DrawableObject(
    private val animation: Animation,
    private val shape: IShape,
    private val color: Color,
    position: Vector2 = Vector2.ZERO
) : Drawable {
    val boundingBox
        get() = shape.getBoundingBox()
    var position = position
        internal set(value) {
            val diff = value - position
            shape.translate(diff)
            field = value
        }

    fun rotate(angleRad: Float) {
        shape.rotate(position, angleRad)
    }

    final override fun render(g2d: Graphics2D) {
        g2d.color = color
        g2d.fill(shape.toAWTShape())
    }
}