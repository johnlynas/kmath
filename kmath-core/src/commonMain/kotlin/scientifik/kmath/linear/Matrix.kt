package scientifik.kmath.linear

import scientifik.kmath.operations.RealField
import scientifik.kmath.operations.Ring
import scientifik.kmath.structures.*
import scientifik.kmath.structures.Buffer.Companion.DoubleBufferFactory
import scientifik.kmath.structures.Buffer.Companion.boxing


interface MatrixContext<T : Any, R : Ring<T>> {
    /**
     * The ring context for matrix elements
     */
    val ring: R

    /**
     * Produce a matrix with this context and given dimensions
     */
    fun produce(rows: Int, columns: Int, initializer: (i: Int, j: Int) -> T): Matrix<T>

    /**
     * Produce a point compatible with matrix space
     */
    fun point(size: Int, initializer: (Int) -> T): Point<T>

    fun scale(a: Matrix<T>, k: Number): Matrix<T> {
        //TODO create a special wrapper class for scaled matrices
        return produce(a.rowNum, a.colNum) { i, j -> ring.run { a[i, j] * k } }
    }

    infix fun Matrix<T>.dot(other: Matrix<T>): Matrix<T> {
        //TODO add typed error
        if (this.colNum != other.rowNum) error("Matrix dot operation dimension mismatch: ($rowNum, $colNum) x (${other.rowNum}, ${other.colNum})")
        return produce(rowNum, other.colNum) { i, j ->
            val row = rows[i]
            val column = other.columns[j]
            with(ring) {
                row.asSequence().zip(column.asSequence(), ::multiply).sum()
            }
        }
    }

    infix fun Matrix<T>.dot(vector: Point<T>): Point<T> {
        //TODO add typed error
        if (this.colNum != vector.size) error("Matrix dot vector operation dimension mismatch: ($rowNum, $colNum) x (${vector.size})")
        return point(rowNum) { i ->
            val row = rows[i]
            with(ring) {
                row.asSequence().zip(vector.asSequence(), ::multiply).sum()
            }
        }
    }

    operator fun Matrix<T>.unaryMinus() =
        produce(rowNum, colNum) { i, j -> ring.run { -get(i, j) } }

    operator fun Matrix<T>.plus(b: Matrix<T>): Matrix<T> {
        if (rowNum != b.rowNum || colNum != b.colNum) error("Matrix operation dimension mismatch. [$rowNum,$colNum] + [${b.rowNum},${b.colNum}]")
        return produce(rowNum, colNum) { i, j -> ring.run { get(i, j) + b[i, j] } }
    }

    operator fun Matrix<T>.minus(b: Matrix<T>): Matrix<T> {
        if (rowNum != b.rowNum || colNum != b.colNum) error("Matrix operation dimension mismatch. [$rowNum,$colNum] - [${b.rowNum},${b.colNum}]")
        return produce(rowNum, colNum) { i, j -> ring.run { get(i, j) + b[i, j] } }
    }

    operator fun Matrix<T>.times(number: Number): Matrix<T> =
        produce(rowNum, colNum) { i, j -> ring.run { get(i, j) * number } }

    operator fun Number.times(m: Matrix<T>): Matrix<T> = m * this


    companion object {
        /**
         * Non-boxing double matrix
         */
        val real: MatrixContext<Double, RealField> = StructureMatrixContext(RealField, DoubleBufferFactory)

        /**
         * A structured matrix with custom buffer
         */
        fun <T : Any, R : Ring<T>> buffered(ring: R, bufferFactory: BufferFactory<T> = ::boxing): MatrixContext<T, R> =
            StructureMatrixContext(ring, bufferFactory)

        /**
         * Automatic buffered matrix, unboxed if it is possible
         */
        inline fun <reified T : Any, R : Ring<T>> auto(ring: R): MatrixContext<T, R> =
            buffered(ring, Buffer.Companion::auto)
    }
}

/**
 * A marker interface representing some matrix feature like diagonal, sparce, zero, etc. Features used to optimize matrix
 * operations performance in some cases.
 */
interface MatrixFeature

object DiagonalFeature : MatrixFeature

object ZeroFeature : MatrixFeature

object UnitFeature : MatrixFeature

interface InverseMatrixFeature<T : Any> : MatrixFeature {
    val inverse: Matrix<T>
}

interface DeterminantFeature<T : Any> : MatrixFeature {
    val determinant: T
}

/**
 * Specialized 2-d structure
 */
interface Matrix<T : Any> : NDStructure<T> {
    val rowNum: Int
    val colNum: Int

    val features: Set<MatrixFeature>

    operator fun get(i: Int, j: Int): T

    override fun get(index: IntArray): T = get(index[0], index[1])

    override val shape: IntArray get() = intArrayOf(rowNum, colNum)

    val rows: Point<Point<T>>
        get() = VirtualBuffer(rowNum) { i ->
            VirtualBuffer(colNum) { j -> get(i, j) }
        }

    val columns: Point<Point<T>>
        get() = VirtualBuffer(colNum) { j ->
            VirtualBuffer(rowNum) { i -> get(i, j) }
        }

    override fun elements(): Sequence<Pair<IntArray, T>> = sequence {
        for (i in (0 until rowNum)) {
            for (j in (0 until colNum)) {
                yield(intArrayOf(i, j) to get(i, j))
            }
        }
    }

    companion object {
        fun real(rows: Int, columns: Int, initializer: (Int, Int) -> Double) =
            MatrixContext.real.produce(rows, columns, initializer)
    }
}

/**
 * Diagonal matrix of ones. The matrix is virtual no actual matrix is created
 */
fun <T : Any, R : Ring<T>> MatrixContext<T, R>.one(rows: Int, columns: Int): Matrix<T> {
    return object : Matrix<T> {
        override val rowNum: Int get() = rows
        override val colNum: Int get() = columns
        override val features: Set<MatrixFeature> get() = setOf(DiagonalFeature, UnitFeature)
        override fun get(i: Int, j: Int): T = if (i == j) ring.one else ring.zero
    }
}

/**
 * A virtual matrix of zeroes
 */
fun <T : Any, R : Ring<T>> MatrixContext<T, R>.zero(rows: Int, columns: Int): Matrix<T> {
    return object : Matrix<T> {
        override val rowNum: Int get() = rows
        override val colNum: Int get() = columns
        override val features: Set<MatrixFeature> get() = setOf(ZeroFeature)
        override fun get(i: Int, j: Int): T = ring.zero
    }
}


inline class TransposedMatrix<T : Any>(val original: Matrix<T>) : Matrix<T> {
    override val rowNum: Int get() = original.colNum
    override val colNum: Int get() = original.rowNum
    override val features: Set<MatrixFeature> get() = emptySet() //TODO retain some features

    override fun get(i: Int, j: Int): T = original[j, i]

    override fun elements(): Sequence<Pair<IntArray, T>> =
        original.elements().map { (key, value) -> intArrayOf(key[1], key[0]) to value }
}

/**
 * Create a virtual transposed matrix without copying anything. `A.transpose().transpose() === A`
 */
fun <T : Any, R : Ring<T>> Matrix<T>.transpose(): Matrix<T> {
    return if (this is TransposedMatrix) {
        original
    } else {
        TransposedMatrix(this)
    }
}