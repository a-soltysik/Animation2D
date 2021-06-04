package com.github.a_soltysik.animation2d.drawables

import com.github.a_soltysik.animation2d.drawables.geometry.shapes.IShape
import com.github.a_soltysik.animation2d.math.Vector2
import java.awt.Color
import java.awt.Graphics2D

abstract class DrawableObject(
    private val shape: IShape,
    private val color: Color,
    position: Vector2 = Vector2.ZERO
) : Drawable {
    val boundingBox
        get() = shape.getBoundingBox()
    var position = position
        protected set(value) {
            moveTo(position)
            field = value
        }

    private fun moveTo(newPosition: Vector2) {
        val diff = newPosition - position
        shape.translate(diff)
        position = Vector2(newPosition)
    }

    fun rotate(angleRad: Float) {
        shape.rotate(position, angleRad)
    }

    override fun render(g2d: Graphics2D) {
        g2d.color = color
        g2d.fill(shape.toAWTShape())
    }
}