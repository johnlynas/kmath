package scientifik.memory

/**
 * A specification to read or write custom objects with fixed size in bytes.
 *
 * @param T the type of object this spec manages.
 */
interface MemorySpec<T : Any> {
    /**
     * Size of [T] in bytes after serialization.
     */
    val objectSize: Int

    /**
     * Reads the object starting from [offset].
     */
    fun MemoryReader.read(offset: Int): T

    // TODO consider thread safety

    /**
     * Writes the object [value] starting from [offset].
     */
    fun MemoryWriter.write(offset: Int, value: T)
}

/**
 * Reads the object with [spec] starting from [offset].
 */
fun <T : Any> MemoryReader.read(spec: MemorySpec<T>, offset: Int): T = with(spec) { read(offset) }

/**
 * Writes the object [value] with [spec] starting from [offset].
 */
fun <T : Any> MemoryWriter.write(spec: MemorySpec<T>, offset: Int, value: T): Unit = with(spec) { write(offset, value) }

/**
 * Reads array of [size] objects mapped by [spec] at certain [offset].
 */
inline fun <reified T : Any> MemoryReader.readArray(spec: MemorySpec<T>, offset: Int, size: Int): Array<T> =
    Array(size) { i ->
        spec.run {
            read(offset + i * objectSize)
        }
    }

/**
 * Writes [array] of objects mapped by [spec] at certain [offset].
 */
fun <T : Any> MemoryWriter.writeArray(spec: MemorySpec<T>, offset: Int, array: Array<T>): Unit =
    with(spec) { array.indices.forEach { i -> write(offset + i * objectSize, array[i]) } }

// TODO It is possible to add elastic MemorySpec with unknown object size
