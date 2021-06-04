package com.github.a_soltysik.animation2d.drawables.geometry.shapes

import com.github.a_soltysik.animation2d.math.Rectangle
import com.github.a_soltysik.animation2d.math.Vector2

class DRectangle private constructor(vertices: Array<Vector2>) : DPolygon(vertices, true) {
    constructor(rectangle: Rectangle) : this(
        arrayOf(
            rectangle.min,
            Vector2(rectangle.max.x, rectangle.min.y),
            rectangle.max,
            Vector2(rectangle.min.x, rectangle.max.y)
        )
    )
}