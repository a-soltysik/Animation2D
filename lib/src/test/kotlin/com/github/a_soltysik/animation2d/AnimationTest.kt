package com.github.a_soltysik.animation2d

import com.github.a_soltysik.animation2d.drawables.geometry.debug.LogRectangle
import com.github.a_soltysik.animation2d.drawables.geometry.debug.LogSegment
import com.github.a_soltysik.animation2d.math.Rectangle
import com.github.a_soltysik.animation2d.math.Vector2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class AnimationTest {
    @Test
    fun addObject() {
        val animation = Animation()
        val rectangle = LogRectangle(Rectangle(max = Vector2(5f, 5f)))
        val segment = LogSegment(Vector2.ZERO, Vector2(5f, 5f))

        animation.addObject(rectangle, 1)
        animation.addObject(segment, 1)
        animation.addObject(rectangle, 0)

        assertEquals(3, animation.getAllObjects().size)
    }

    @Test
    fun removeObject() {
        val animation = Animation()
        val rectangle1 = LogRectangle(Rectangle(max = Vector2(5f, 5f)))
        val rectangle2 = LogRectangle(Rectangle(max = Vector2(5f, 5f)))
        val segment = LogSegment(Vector2.ZERO, Vector2(5f, 5f))

        animation.addObject(rectangle1, 1)
        animation.addObject(segment, 1)
        animation.addObject(rectangle1, 0)
        animation.addObject(rectangle2, 0)

        animation.removeObject(segment)
        assertEquals(3, animation.getAllObjects().size)
        animation.removeObject(rectangle1)
        assertEquals(1, animation.getAllObjects().size)
    }

    @Test
    fun removeObjectWithType() {
        val animation = Animation()
        val rectangle1 = LogRectangle(Rectangle(max = Vector2(5f, 5f)))
        val rectangle2 = LogRectangle(Rectangle(max = Vector2(5f, 5f)))
        val rectangle3 = LogRectangle(Rectangle(max = Vector2(5f, 5f)))
        val rectangle4 = LogRectangle(Rectangle(max = Vector2(5f, 5f)))
        val rectangle5 = LogRectangle(Rectangle(max = Vector2(5f, 5f)))
        val segment = LogSegment(Vector2.ZERO, Vector2(5f, 5f))

        animation.addObject(segment, 1)
        animation.addObject(rectangle1, 1)
        animation.addObject(rectangle2, 2)
        animation.addObject(rectangle3, 3)
        animation.addObject(rectangle4, 4)
        animation.addObject(rectangle5, 5)

        assertFails { animation.removeObjects<LogSegment>(-1)}
        animation.removeObjects<LogSegment>()
        assertEquals(5, animation.getAllObjects().size)
        animation.removeObjects<LogRectangle>(1)
        assertEquals(4, animation.getAllObjects().size)
        animation.removeObjects<LogRectangle>()
        assertEquals(0, animation.getAllObjects().size)
    }
}