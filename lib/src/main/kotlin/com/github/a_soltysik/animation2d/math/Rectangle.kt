package com.github.a_soltysik.animation2d.math

import kotlin.math.max
import kotlin.math.min

class Rectangle(var min: Vector2 = Vector2.ZERO, var max: Vector2) {
    constructor(rectangle: Rectangle) : this(rectangle.min, rectangle.max)

    fun intersects(rectangle: Rectangle): Boolean {
        val leftX = max(min.x, rectangle.min.x)
        val rightX = min(max.x, rectangle.max.x)
        val topY = min(min.y, rectangle.min.y)
        val bottomY = max(max.y, rectangle.max.y)
        return leftX < rightX && topY < bottomY
    }

    fun isInside(rectangle: Rectangle) =
         min.x >= rectangle.min.x && min.y >= rectangle.min.y && max.x <= rectangle.max.x && max.y <= rectangle.max.y

    override fun toString() = "Rectangle{min=$min, max=$max}"
}