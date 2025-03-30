package kul.dataframework.core

import kotlin.reflect.KProperty

/**
 * @author Cco0lL created 9/21/24 5:14PM
 **/

open class ParameterizedObject : ParameterContainer<Parameter>(), Iterable<Parameter> {

    protected val parameters = LinkedHashMap<String, Parameter>(8)

    override operator fun <FUN_P : Parameter> get(key: String): FUN_P? {
        @Suppress("UNCHECKED_CAST")
        return parameters[key] as FUN_P
    }

    fun <P : Parameter> get(property: KProperty<*>): P? {
        return get(property.name)
    }

    internal fun _injectParameter(param: Parameter) {
        parameters[param.name] = param
    }

    fun read(other: ParameterizedObject) {
        other.readIt {
            val parameters = parameters
            val otherParameters = other.parameters
            for ((key, param) in parameters) {
                param.readValueFromAnotherParameter(otherParameters[key]!!)
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
        private var backingItr = parameters.values.iterator()
        override fun hasNext() = backingItr.hasNext()
        override fun next() = backingItr.next()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ParameterizedObject
        return parameters == other.parameters
    }

    override fun hashCode(): Int {
        val hashCode = parameters.hashCode()
        return hashCode
    }

    override fun toString(): String {
        return parameters.toString()
    }
}
