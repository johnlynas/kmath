package kscience.kmath.gsl

import kotlinx.cinterop.*
import kscience.kmath.linear.*
import org.gnu.gsl.*

internal class GslRealMatrix(
    override val nativeHandle: CPointer<gsl_matrix>,
    features: Set<MatrixFeature> = emptySet()
) : GslMatrix<Double, gsl_matrix>() {
    override val rowNum: Int
        get() = nativeHandle.pointed.size1.toInt()

    override val colNum: Int
        get() = nativeHandle.pointed.size2.toInt()

    override val features: Set<MatrixFeature> = features

    override fun suggestFeature(vararg features: MatrixFeature): GslRealMatrix =
        GslRealMatrix(nativeHandle, this.features + features)

    override operator fun get(i: Int, j: Int): Double = gsl_matrix_get(nativeHandle, i.toULong(), j.toULong())

    override operator fun set(i: Int, j: Int, value: Double): Unit =
        gsl_matrix_set(nativeHandle, i.toULong(), j.toULong(), value)

    override fun copy(): GslRealMatrix {
        val new = requireNotNull(gsl_matrix_alloc(rowNum.toULong(), colNum.toULong()))
        gsl_matrix_memcpy(new, nativeHandle)
        return GslRealMatrix(new, features)
    }

    override fun close(): Unit = gsl_matrix_free(nativeHandle)

    override fun equals(other: Any?): Boolean {
        if (other is GslRealMatrix) return gsl_matrix_equal(nativeHandle, other.nativeHandle) == 1
        return super.equals(other)
    }
}

internal class GslFloatMatrix(
    override val nativeHandle: CPointer<gsl_matrix_float>,
    features: Set<MatrixFeature> = emptySet()
) : GslMatrix<Float, gsl_matrix_float>() {
    override val rowNum: Int
        get() = nativeHandle.pointed.size1.toInt()

    override val colNum: Int
        get() = nativeHandle.pointed.size2.toInt()

    override val features: Set<MatrixFeature> = features

    override fun suggestFeature(vararg features: MatrixFeature): GslFloatMatrix =
        GslFloatMatrix(nativeHandle, this.features + features)

    override operator fun get(i: Int, j: Int): Float = gsl_matrix_float_get(nativeHandle, i.toULong(), j.toULong())

    override operator fun set(i: Int, j: Int, value: Float): Unit =
        gsl_matrix_float_set(nativeHandle, i.toULong(), j.toULong(), value)

    override fun copy(): GslFloatMatrix {
        val new = requireNotNull(gsl_matrix_float_alloc(rowNum.toULong(), colNum.toULong()))
        gsl_matrix_float_memcpy(new, nativeHandle)
        return GslFloatMatrix(new, features)
    }

    override fun close(): Unit = gsl_matrix_float_free(nativeHandle)

    override fun equals(other: Any?): Boolean {
        if (other is GslFloatMatrix) return gsl_matrix_float_equal(nativeHandle, other.nativeHandle) == 1
        return super.equals(other)
    }
}

internal class GslShortMatrix(
    override val nativeHandle: CPointer<gsl_matrix_short>,
    features: Set<MatrixFeature> = emptySet()
) : GslMatrix<Short, gsl_matrix_short>() {
    override val rowNum: Int
        get() = nativeHandle.pointed.size1.toInt()

    override val colNum: Int
        get() = nativeHandle.pointed.size2.toInt()

    override val features: Set<MatrixFeature> = features

    override fun suggestFeature(vararg features: MatrixFeature): GslShortMatrix =
        GslShortMatrix(nativeHandle, this.features + features)

    override operator fun get(i: Int, j: Int): Short = gsl_matrix_short_get(nativeHandle, i.toULong(), j.toULong())

    override operator fun set(i: Int, j: Int, value: Short): Unit =
        gsl_matrix_short_set(nativeHandle, i.toULong(), j.toULong(), value)

    override fun copy(): GslShortMatrix {
        val new = requireNotNull(gsl_matrix_short_alloc(rowNum.toULong(), colNum.toULong()))
        gsl_matrix_short_memcpy(new, nativeHandle)
        return GslShortMatrix(new, features)
    }

    override fun close(): Unit = gsl_matrix_short_free(nativeHandle)

    override fun equals(other: Any?): Boolean {
        if (other is GslShortMatrix) return gsl_matrix_short_equal(nativeHandle, other.nativeHandle) == 1
        return super.equals(other)
    }
}

internal class GslUShortMatrix(
    override val nativeHandle: CPointer<gsl_matrix_ushort>,
    features: Set<MatrixFeature> = emptySet()
) : GslMatrix<UShort, gsl_matrix_ushort>() {
    override val rowNum: Int
        get() = nativeHandle.pointed.size1.toInt()

    override val colNum: Int
        get() = nativeHandle.pointed.size2.toInt()

    override val features: Set<MatrixFeature> = features

    override fun suggestFeature(vararg features: MatrixFeature): GslUShortMatrix =
        GslUShortMatrix(nativeHandle, this.features + features)

    override operator fun get(i: Int, j: Int): UShort = gsl_matrix_ushort_get(nativeHandle, i.toULong(), j.toULong())

    override operator fun set(i: Int, j: Int, value: UShort): Unit =
        gsl_matrix_ushort_set(nativeHandle, i.toULong(), j.toULong(), value)

    override fun copy(): GslUShortMatrix {
        val new = requireNotNull(gsl_matrix_ushort_alloc(rowNum.toULong(), colNum.toULong()))
        gsl_matrix_ushort_memcpy(new, nativeHandle)
        return GslUShortMatrix(new, features)
    }

    override fun close(): Unit = gsl_matrix_ushort_free(nativeHandle)

    override fun equals(other: Any?): Boolean {
        if (other is GslUShortMatrix) return gsl_matrix_ushort_equal(nativeHandle, other.nativeHandle) == 1
        return super.equals(other)
    }
}

internal class GslLongMatrix(
    override val nativeHandle: CPointer<gsl_matrix_long>,
    features: Set<MatrixFeature> = emptySet()
) : GslMatrix<Long, gsl_matrix_long>() {
    override val rowNum: Int
        get() = nativeHandle.pointed.size1.toInt()

    override val colNum: Int
        get() = nativeHandle.pointed.size2.toInt()

    override val features: Set<MatrixFeature> = features

    override fun suggestFeature(vararg features: MatrixFeature): GslLongMatrix =
        GslLongMatrix(nativeHandle, this.features + features)

    override operator fun get(i: Int, j: Int): Long = gsl_matrix_long_get(nativeHandle, i.toULong(), j.toULong())

    override operator fun set(i: Int, j: Int, value: Long): Unit =
        gsl_matrix_long_set(nativeHandle, i.toULong(), j.toULong(), value)

    override fun copy(): GslLongMatrix {
        val new = requireNotNull(gsl_matrix_long_alloc(rowNum.toULong(), colNum.toULong()))
        gsl_matrix_long_memcpy(new, nativeHandle)
        return GslLongMatrix(new, features)
    }

    override fun close(): Unit = gsl_matrix_long_free(nativeHandle)

    override fun equals(other: Any?): Boolean {
        if (other is GslLongMatrix) return gsl_matrix_long_equal(nativeHandle, other.nativeHandle) == 1
        return super.equals(other)
    }
}

internal class GslULongMatrix(
    override val nativeHandle: CPointer<gsl_matrix_ulong>,
    features: Set<MatrixFeature> = emptySet()
) : GslMatrix<ULong, gsl_matrix_ulong>() {
    override val rowNum: Int
        get() = nativeHandle.pointed.size1.toInt()

    override val colNum: Int
        get() = nativeHandle.pointed.size2.toInt()

    override val features: Set<MatrixFeature> = features

    override fun suggestFeature(vararg features: MatrixFeature): GslULongMatrix =
        GslULongMatrix(nativeHandle, this.features + features)

    override operator fun get(i: Int, j: Int): ULong = gsl_matrix_ulong_get(nativeHandle, i.toULong(), j.toULong())

    override operator fun set(i: Int, j: Int, value: ULong): Unit =
        gsl_matrix_ulong_set(nativeHandle, i.toULong(), j.toULong(), value)

    override fun copy(): GslULongMatrix {
        val new = requireNotNull(gsl_matrix_ulong_alloc(rowNum.toULong(), colNum.toULong()))
        gsl_matrix_ulong_memcpy(new, nativeHandle)
        return GslULongMatrix(new, features)
    }

    override fun close(): Unit = gsl_matrix_ulong_free(nativeHandle)

    override fun equals(other: Any?): Boolean {
        if (other is GslULongMatrix) return gsl_matrix_ulong_equal(nativeHandle, other.nativeHandle) == 1
        return super.equals(other)
    }
}

internal class GslIntMatrix(
    override val nativeHandle: CPointer<gsl_matrix_int>,
    features: Set<MatrixFeature> = emptySet()
) : GslMatrix<Int, gsl_matrix_int>() {
    override val rowNum: Int
        get() = nativeHandle.pointed.size1.toInt()

    override val colNum: Int
        get() = nativeHandle.pointed.size2.toInt()

    override val features: Set<MatrixFeature> = features

    override fun suggestFeature(vararg features: MatrixFeature): GslIntMatrix =
        GslIntMatrix(nativeHandle, this.features + features)

    override operator fun get(i: Int, j: Int): Int = gsl_matrix_int_get(nativeHandle, i.toULong(), j.toULong())

    override operator fun set(i: Int, j: Int, value: Int): Unit =
        gsl_matrix_int_set(nativeHandle, i.toULong(), j.toULong(), value)

    override fun copy(): GslIntMatrix {
        val new = requireNotNull(gsl_matrix_int_alloc(rowNum.toULong(), colNum.toULong()))
        gsl_matrix_int_memcpy(new, nativeHandle)
        return GslIntMatrix(new, features)
    }

    override fun close(): Unit = gsl_matrix_int_free(nativeHandle)

    override fun equals(other: Any?): Boolean {
        if (other is GslIntMatrix) return gsl_matrix_int_equal(nativeHandle, other.nativeHandle) == 1
        return super.equals(other)
    }
}

internal class GslUIntMatrix(
    override val nativeHandle: CPointer<gsl_matrix_uint>,
    features: Set<MatrixFeature> = emptySet()
) : GslMatrix<UInt, gsl_matrix_uint>() {
    override val rowNum: Int
        get() = nativeHandle.pointed.size1.toInt()

    override val colNum: Int
        get() = nativeHandle.pointed.size2.toInt()

    override val features: Set<MatrixFeature> = features

    override fun suggestFeature(vararg features: MatrixFeature): GslUIntMatrix =
        GslUIntMatrix(nativeHandle, this.features + features)

    override operator fun get(i: Int, j: Int): UInt = gsl_matrix_uint_get(nativeHandle, i.toULong(), j.toULong())

    override operator fun set(i: Int, j: Int, value: UInt): Unit =
        gsl_matrix_uint_set(nativeHandle, i.toULong(), j.toULong(), value)

    override fun copy(): GslUIntMatrix {
        val new = requireNotNull(gsl_matrix_uint_alloc(rowNum.toULong(), colNum.toULong()))
        gsl_matrix_uint_memcpy(new, nativeHandle)
        return GslUIntMatrix(new, features)
    }

    override fun close(): Unit = gsl_matrix_uint_free(nativeHandle)

    override fun equals(other: Any?): Boolean {
        if (other is GslUIntMatrix) return gsl_matrix_uint_equal(nativeHandle, other.nativeHandle) == 1
        return super.equals(other)
    }
}
