package com.github.a_soltysik.animation2d.math

import com.github.a_soltysik.animation2d.Utils
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vector2(var x: Float = 0f, var y: Float = 0f) {

    constructor(v: Vector2) : this(v.x, v.y)

    val magnitude: Float
        get() = sqrt(x * x + y * y)
    val normalized: Vector2
        get() {
            val magnitude = magnitude
            require(magnitude != 0f)
            return Vector2(x / magnitude, y / magnitude)
        }

    companion object {
        val ZERO = Vector2()

        fun dot(v1: Vector2, v2: Vector2) = v1.x * v2.x + v1.y * v2.y
        fun distance(v1: Vector2, v2: Vector2) =
            sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y))
    }

    operator fun plus(v: Vector2) = Vector2(x + v.x, y + v.y)
    operator fun minus(v: Vector2) = Vector2(x - v.x, y - v.y)
    operator fun times(a: Float) = Vector2(x * a, y * a)
    operator fun div(a: Float): Vector2 {
        require(a != 0f) { "The divisor cannot be 0" }
        return Vector2(x / a, y / a)
    }

    fun isZero() = x == 0f && y == 0f

    fun limit(newMagnitude: Float) {
        if (magnitude > newMagnitude) {
            normalize()
            x *= newMagnitude
            y *= newMagnitude
        }
    }

    fun limited(newMagnitude: Float): Vector2 {
        if (magnitude > newMagnitude) {
            return normalized * newMagnitude
        }
        return Vector2(this)
    }

    fun normalize() {
        val magnitude = magnitude
        require(magnitude != 0f)
        x /= magnitude
        y /= magnitude
    }

    fun cross(v: Vector2) = x * v.y - y * v.x

    fun rotate(center: Vector2, angleRad: Float) {
        val dx: Float = x - center.x
        val dy: Float = y - center.y
        val cos = cos(angleRad)
        val sin = sin(angleRad)
        x = center.x + (dx * cos - dy * sin)
        y = center.y + (dx * sin + dy * cos)
    }

    fun rotated(center: Vector2, angleRad: Float): Vector2 {
        val dx: Float = x - center.x
        val dy: Float = y - center.y
        val cos = cos(angleRad)
        val sin = sin(angleRad)
        return Vector2(
            center.x + (dx * cos - dy * sin),
            center.y + (dx * sin + dy * cos)
        )
    }

    fun directAngle(vec: Vector2): Float {
        require(!isZero() && !vec.isZero()) { "Angle between zero vectors is undefined" }
        val angle = Utils.fastAtan2(cross(vec), dot(this, vec))
        return if (angle >= 0) {
            angle
        } else {
            2 * Utils.PI + angle
        }
    }

    fun isInside(rectangle: Rectangle) =
        x >= rectangle.min.x && x <= rectangle.max.x && y >= rectangle.min.y && y <= rectangle.max.y

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val vector2 = other as Vector2
        return Utils.isEqual(vector2.x, x) && Utils.isEqual(vector2.y, y)
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    override fun toString() = "Vector2{x=$x, y=$y}"


}