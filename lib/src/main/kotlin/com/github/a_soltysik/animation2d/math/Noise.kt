package com.github.a_soltysik.animation2d.math

import kotlin.experimental.xor

class Noise (private var seed: Long = 0L){

    private val perm: ShortArray = ShortArray(PSIZE)
    private val permGrad2: Array<Grad2> = Array(PSIZE) { Grad2(0.0, 0.0) }

    init {
        val source = ShortArray(PSIZE)
        for (i in 0 until PSIZE) source[i] = i.toShort()
        for (i in PSIZE - 1 downTo 0) {
            seed = seed * 6364136223846793005L + 1442695040888963407L
            var r = ((seed + 31) % (i + 1)).toInt()
            if (r < 0) r += i + 1
            perm[i] = source[r]
            permGrad2[i] = GRADIENTS_2D[perm[i].toInt()]
            source[r] = source[i]
        }
    }

    /*
     * Noise Evaluators
     */
    fun noise(x: Double, y: Double): Double {

        // Get points for A2* lattice
        val s = 0.366025403784439 * (x + y)
        val xs = x + s
        val ys = y + s
        return noiseBase(xs, ys)
    }


    /**
     * 2D Simplex noise, with Y pointing down the main diagonal.
     * Might be better for a 2D sandbox style game, where Y is vertical.
     * Probably slightly less optimal for heightmaps or continent maps.
     */
    fun noise_XBeforeY(x: Double, y: Double): Double {

        // Skew transform and rotation baked into one.
        val xx = x * 0.7071067811865476
        val yy = y * 1.224744871380249
        return noiseBase(yy + xx, yy - xx)
    }

    /**
     * 2D Simplex noise base.
     * Lookup table implementation inspired by DigitalShadow.
     */
    private fun noiseBase(xs: Double, ys: Double): Double {
        var value = 0.0

        // Get base points and offsets
        val xsb: Int = fastFloor(xs)
        val ysb: Int = fastFloor(ys)
        val xsi = xs - xsb
        val ysi = ys - ysb

        // Index to point list
        val index = ((ysi - xsi) / 2 + 1).toInt()
        val ssi = (xsi + ysi) * -0.211324865405187
        val xi = xsi + ssi
        val yi = ysi + ssi

        // Point contributions
        for (i in 0..2) {
            val c = LOOKUP_2D[index + i]
            val dx = xi + c.dx
            val dy = yi + c.dy
            var attn = 0.5 - dx * dx - dy * dy
            if (attn <= 0) continue
            val pxm = xsb + c.xsv and PMASK
            val pym = ysb + c.ysv and PMASK
            val (dx1, dy1) = permGrad2[(perm[pxm] xor pym.toShort()).toInt()]
            val extrapolation = dx1 * dx + dy1 * dy
            attn *= attn
            value += attn * attn * extrapolation
        }
        return value
    }

    companion object {
        private const val PSIZE = 2048
        private const val PMASK = 2047
        private const val N2 = 0.01001634121365712
        private val GRADIENTS_2D: Array<Grad2> = Array(PSIZE) {Grad2(0.0,0.0)}
        private val LOOKUP_2D: Array<LatticePoint2D> = arrayOf(
            LatticePoint2D(1, 0),
            LatticePoint2D(0, 0),
            LatticePoint2D(1, 1),
            LatticePoint2D(0, 1))

        private fun fastFloor(x: Double): Int {
            val xi = x.toInt()
            return if (x < xi) xi - 1 else xi
        }

        private data class Grad2(var dx: Double, var dy: Double)
        private class LatticePoint2D(var xsv: Int, var ysv: Int) {
            var dx: Double
            var dy: Double

            init {
                val ssv = (xsv + ysv) * -0.211324865405187
                dx = -xsv - ssv
                dy = -ysv - ssv
            }
        }

        init {
            val grad2 = arrayOf(
                Grad2(0.130526192220052, 0.99144486137381),
                Grad2(0.38268343236509, 0.923879532511287),
                Grad2(0.608761429008721, 0.793353340291235),
                Grad2(0.793353340291235, 0.608761429008721),
                Grad2(0.923879532511287, 0.38268343236509),
                Grad2(0.99144486137381, 0.130526192220051),
                Grad2(0.99144486137381, -0.130526192220051),
                Grad2(0.923879532511287, -0.38268343236509),
                Grad2(0.793353340291235, -0.60876142900872),
                Grad2(0.608761429008721, -0.793353340291235),
                Grad2(0.38268343236509, -0.923879532511287),
                Grad2(0.130526192220052, -0.99144486137381),
                Grad2(-0.130526192220052, -0.99144486137381),
                Grad2(-0.38268343236509, -0.923879532511287),
                Grad2(-0.608761429008721, -0.793353340291235),
                Grad2(-0.793353340291235, -0.608761429008721),
                Grad2(-0.923879532511287, -0.38268343236509),
                Grad2(-0.99144486137381, -0.130526192220052),
                Grad2(-0.99144486137381, 0.130526192220051),
                Grad2(-0.923879532511287, 0.38268343236509),
                Grad2(-0.793353340291235, 0.608761429008721),
                Grad2(-0.608761429008721, 0.793353340291235),
                Grad2(-0.38268343236509, 0.923879532511287),
                Grad2(-0.130526192220052, 0.99144486137381)
            )

            for (i in grad2.indices) {
                grad2[i].dx /= N2
                grad2[i].dy /= N2
            }
            for (i in 0 until PSIZE) {
                GRADIENTS_2D[i] = grad2[i % grad2.size]
            }
        }
    }
}