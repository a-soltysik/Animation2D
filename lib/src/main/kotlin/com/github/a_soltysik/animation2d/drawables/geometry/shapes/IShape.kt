package com.github.a_soltysik.animation2d.drawables.geometry.shapes

import com.github.a_soltysik.animation2d.math.Rectangle
import com.github.a_soltysik.animation2d.math.Vector2
import java.awt.Shape

interface IShape {
    fun toAWTShape(): Shape
    fun translate(diff: Vector2)
    fun rotate(center: Vector2, angleRad: Float)
    fun getBoundingBox(): Rectangle
}