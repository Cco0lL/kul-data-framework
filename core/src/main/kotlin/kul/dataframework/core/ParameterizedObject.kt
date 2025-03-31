package kul.dataframework.core

import java.util.StringJoiner
import kotlin.collections.HashMap
import kotlin.math.min
import kotlin.reflect.KProperty

/**
 * @author Cco0lL created 9/21/24 5:14PM
 **/

open class ParameterizedObject : ParameterContainer<Parameter>(), Iterable<Parameter> {

    private var size = 0
    private var parameters = arrayOfNulls<Parameter>(CHUNK_PARAMS_CAPACITY)

    override operator fun <FUN_P : Parameter> get(name: String): FUN_P? {
        @Suppress("UNCHECKED_CAST")
        return find { it.name == name } as? FUN_P
    }

    fun <P : Parameter> get(property: KProperty<*>): P? {
        return get(property.name)
    }

    internal fun _injectParameter(param: Parameter) {
        val ps = parameters
        if (size == ps.size) {
            val nps = arrayOfNulls<Parameter>(ps.size + CHUNK_PARAMS_CAPACITY)
            System.arraycopy(ps, 0, nps, 0, ps.size)
            parameters = nps
        }
        ps[size++] = param
    }

    fun read(other: ParameterizedObject) {
        other.readIt {
            if (other::class === this::class) {
                for (i in 0 until size) {
                    val thisParam = parameters[size]!!
                    val otherParam = it.parameters[size]!!
                    thisParam.readValueFromAnotherParameter(otherParam)
                }
            } else {
                val otherParams = other.associateByTo(HashMap((size * 0.75f + 1).toInt())) { p -> p.name }
                for (param in this) {
                    otherParams[param.name]?.let { otherParam ->
                        param.readValueFromAnotherParameter(otherParam)
                    }
                }
            }
        }
    }

    open fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, obj: OBJECT) {
        forEach { it.read(readCtx, readCtx.getElement(it.name, obj)) }
    }

    open fun <ELEMENT, OBJECT> write(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT) {
        forEach { writeCtx.writeElement(it.name, obj, it.toElement(writeCtx, obj)) }
    }

    override fun iterator() = object : Iterator<Parameter> {
        private var cursor = 0
        override fun hasNext() = cursor != size
        override fun next() = parameters[cursor++]!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterizedObject

        if (size != other.size) {
            return false
        }

        val ps = parameters
        val otherPs = other.parameters

        for (i in 0 until size) {
            if (ps[i]!! != otherPs[i]!!)
                return false
        }

        return true
    }

    override fun hashCode(): Int {
        var hashCode = 1
        val ps = parameters
        for (i in 0 until size) {
            hashCode = 31 * hashCode + ps[i]!!.hashCode()
        }
        return hashCode
    }

    override fun toString(): String {
        val sj = StringJoiner(", ", "[", "]")
        for (i in 0 until size) {
            sj.add(parameters[i]!!.toString())
        }
        return sj.toString()
    }

    companion object {
        private const val CHUNK_PARAMS_CAPACITY = 1 shl 3
    }
}
