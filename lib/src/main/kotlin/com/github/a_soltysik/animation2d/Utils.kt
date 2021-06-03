package com.github.a_soltysik.animation2d

import kotlin.math.abs
import kotlin.math.max

class Utils private constructor() {

    companion object {
        const val PI = 3.14159265359f
        const val PI_2 = 1.57079632679f

        fun isEqual(f1: Float, f2: Float): Boolean {
            val diff = abs(f1 - f2)
            return if (diff <= 1.0e-5f) {
                true
            } else diff <= 1.0e-5f * max(abs(f1), abs(f2))
        }

        fun fastAtan(x: Float): Float {
            var x = x
            val b = 0.596227f
            return if (x >= 0) (b * x + x * x) / (1 + 2 * b * x + x * x) else {
                x *= -1f
                -(b * x + x * x) / (1 + 2 * b * x + x * x)
            }
        }

        fun fastAtan2(y: Float, x: Float): Float {
            require(y != 0f && x != 0f) {"atan2 is not defined for y and x equal to 0"}
            if (x > 0) {
                return fastAtan(y / x)
            }
            if (x < 0 && y >= 0) {
                return fastAtan(y / x) + PI
            }
            if (x < 0 && y < 0) {
                return fastAtan(y / x) - PI
            }
            if (x == 0f && y > 0) {
                return PI_2
            }
            if (x == 0f && y < 0) {
                return -PI_2
            }
            throw IllegalArgumentException("atan2 is not defined for x=$x and y=$y")
        }
    }
}