package com.github.a_soltysik.animation2d.drawables.geometry.shapes

import com.github.a_soltysik.animation2d.Utils
import com.github.a_soltysik.animation2d.math.Noise
import com.github.a_soltysik.animation2d.math.Rectangle
import com.github.a_soltysik.animation2d.math.Vector2
import java.awt.Shape
import java.awt.geom.Path2D
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class DPolygon(private val vertices: Array<Vector2>) : IShape {
    constructor(numberOfSides: Int, radius: Float, isOnPosition: Boolean = true, random: Boolean = false)
            : this(Array<Vector2>(numberOfSides) { Vector2() }){
        generateRegularPolygon(radius)
        if (random) {
            addNoise()
        }
        if (!isOnPosition) {
            translate(getCenter() * -1f)
        }
    }

    private fun generateRegularPolygon(radius: Float) {
        var angle = 0f
        var i = 0
        while (i < vertices.size) {
            vertices[i] = Vector2(
                sin(angle) * radius,
                cos(angle) * radius
            )
            i++
            angle += 2 * Utils.PI / vertices.size
        }
    }

    private fun addNoise() {
        val noiseResolution = 0.02f
        val noise = Noise(Random.nextLong())
        for (i in vertices.indices) {
            var value = noise.noise(
                (vertices[i].x * noiseResolution).toDouble(),
                (vertices[i].y * noiseResolution).toDouble()
            )
            value = (value + 1) / 2
            value = value * (1.2f - 0.8f) + 0.8f
            vertices[i] = vertices[i] * value.toFloat()
        }
    }

    override fun toAWTShape(): Shape {
        val shape = Path2D.Float()
        shape.moveTo(vertices[0].x.toDouble(), vertices[0].y.toDouble())
        for (i in 1 until vertices.size) {
            shape.lineTo(vertices[i].x.toDouble(), vertices[i].y.toDouble())
        }
        shape.closePath()
        return shape
    }

    override fun translate(diff: Vector2) {
        for (i in vertices.indices) {
            vertices[i] += diff
        }
    }

    override fun rotate(center: Vector2, angleRad: Float) {
        for (i in vertices.indices) {
            if (vertices[i] != center) {
                vertices[i] = vertices[i].rotated(center, angleRad)
            }
        }
    }

    override fun getBoundingBox(): Rectangle {
        val boundingBox = Rectangle(
            Vector2(Float.MAX_VALUE, Float.MAX_VALUE),
            Vector2(Float.MIN_VALUE, Float.MIN_VALUE)
        )
        for (vertex in vertices) {
            if (vertex.x > boundingBox.max.x) {
                boundingBox.max.x = vertex.x
            }
            if (vertex.x < boundingBox.min.x) {
                boundingBox.min.x = vertex.x
            }
            if (vertex.y > boundingBox.max.y) {
                boundingBox.max.y = vertex.y
            }
            if (vertex.y < boundingBox.min.y) {
                boundingBox.min.y = vertex.y
            }
        }
        return boundingBox
    }

    private fun getCenter(): Vector2 {
        var center = Vector2.ZERO
        for(vertex in vertices) {
            center += vertex
        }
        return center / vertices.size.toFloat()
    }
}