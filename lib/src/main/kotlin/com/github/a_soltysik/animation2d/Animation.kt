package com.github.a_soltysik.animation2d

import com.github.a_soltysik.animation2d.drawables.Drawable
import com.github.a_soltysik.animation2d.gui.AnimationPanel
import com.github.a_soltysik.animation2d.gui.FramePanel
import com.github.a_soltysik.animation2d.gui.Logger
import java.awt.Graphics2D
import java.lang.reflect.InvocationTargetException
import java.util.*
import javax.swing.SwingUtilities
import javax.swing.SwingWorker
import kotlin.collections.ArrayList

class Animation(val preferredFps: Int = UNLIMITED_FPS) {
    private val objects: TreeMap<Int, ArrayList<Drawable>> = TreeMap()
    lateinit var animationPanel: AnimationPanel
        internal set
    lateinit var logger: Logger
        internal set
    private var frameTime = 0.0
    private val timeScale = 1_000_000_000
    private var fps = 0

    companion object {
        const val UNLIMITED_FPS = 0
    }

    fun start() {
        if (!this::animationPanel.isInitialized) {
            return
        }
        object : SwingWorker<Void, Void>() {
            override fun doInBackground(): Void? {
                if (preferredFps == UNLIMITED_FPS) {
                    unlimitedLoop()
                } else {
                    loop()
                }
                return null
            }
        }.execute()
    }

    fun addObject(drawable: Drawable, layer: Int) {
        var list = objects[layer]
        if (list == null) {
            list = ArrayList(Collections.singletonList(drawable))
            objects[layer] = list
        } else {
            list.add(drawable)
        }
    }

    private fun update() = objects.forEach { (k, v) ->
            v.parallelStream().forEach{o -> o.update(this, frameTime)}
        }

    internal fun render(g2d: Graphics2D) = objects.forEach {(k, v) ->
        v.stream().forEach{o-> o.render(g2d)}
    }

    private fun render() {
        try {
            SwingUtilities.invokeAndWait {
                with(animationPanel) {
                    this!!.framePanel.paintImmediately(0, 0, framePanel.width, framePanel.height)
                }
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    private fun unlimitedLoop() {
        var currentTime: Long
        var previousTime = System.nanoTime()
        var time: Long = 0
        var frameTimeNanos: Long
        var currentFps = 0
        while (true) {
            update()
            render()
            currentTime = System.nanoTime()
            frameTimeNanos = currentTime - previousTime
            previousTime = currentTime
            time += frameTimeNanos
            frameTime = frameTimeNanos.toDouble() / timeScale
            currentFps++
            if (time >= timeScale) {
                time = 0
                fps = currentFps
                currentFps = 0
            }
        }
    }

    private fun loop() {
        var currentTime: Long
        var previousTime = System.nanoTime()
        var time: Long = 0
        var timeToRender = 0
        var currentFps = 0
        var frameTimeNanos: Long
        val preferredFrameTime: Int = timeScale / preferredFps
        while (true) {
            update()
            if (timeToRender >= preferredFrameTime) {
                render()
                timeToRender = 0
                currentFps++
            }
            currentTime = System.nanoTime()
            frameTimeNanos = currentTime - previousTime
            previousTime = currentTime
            time += frameTimeNanos
            timeToRender += frameTimeNanos.toInt()
            frameTime = frameTimeNanos.toDouble() / timeScale
            if (time > timeScale) {
                time = 0
                fps = currentFps
                currentFps = 0
            }
        }
    }

    fun removeObject(drawable: Drawable) {
        for ((k, v) in objects) {
            v.removeIf{ o -> o === drawable}
        }
    }

    inline fun <reified T : Drawable> removeObjects(number: Int = Int.MAX_VALUE) {
        removeObjects(T::class.java, number)
    }

    fun <T : Drawable> removeObjects(type: Class<T>, number: Int) {
        require(number >= 0) {"number of objects cannot by lower than 0"}
        var counter = 0
        for ((k, v) in objects) {
            var i = 0
            while(i < v.size) {
                if (type.isAssignableFrom(v[i].javaClass)) {
                    v.removeAt(i--)
                    if (++counter >= number) return
                }
                i++
            }
        }
    }

    inline fun <reified T : Drawable> getObjects(): ArrayList<T> {
        return getObjects(T::class.java)
    }

    fun <T : Drawable> getObjects(type: Class<T>): ArrayList<T> {
        val list = ArrayList<T>()
        for ((k, v) in objects) {
            for (drawable in v) {
                if (type.isAssignableFrom(drawable.javaClass)) {
                    list.add(drawable as T);
                }
            }
        }
        return list
    }

    fun getAllObjects(): ArrayList<Drawable> {
        val list = ArrayList<Drawable>()
        for ((k, v) in objects) {
            list.addAll(v)
        }
        return list
    }
}