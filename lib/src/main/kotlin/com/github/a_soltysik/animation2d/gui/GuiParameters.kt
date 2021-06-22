package com.github.a_soltysik.animation2d.gui

class IntParameter(
    val name: String = "",
    value: Int,
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE,
    val tick: Int = 1
) {

    var value = value
        set(value) {
            require(!(value > max || value < min)) { "Value is not between min and max" }
            field = value
        }
}

class FloatParameter(
    val name: String = "",
    value: Float,
    val min: Float = Float.MIN_VALUE,
    val max: Float = Float.MAX_VALUE,
    val tick: Float = 1f
) {

    var value = value
        set(value) {
            require(!(value > max || value < min)) { "Value is not between min and max" }
            field = value
        }
}