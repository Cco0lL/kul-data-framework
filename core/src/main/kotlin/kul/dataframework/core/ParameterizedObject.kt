package kul.dataframework.core

/**
 * @author Cco0lL created 9/21/24 5:14PM
 **/

open class ParameterizedObject(amountOfParameters: Int) : ParameterContainer<Parameter>() {

    private var nextParameterCursor = 0
    protected val parameters = arrayOfNulls<Parameter>(amountOfParameters)

    override operator fun <FUN_P : Parameter> get(key: String): FUN_P? {
        @Suppress("UNCHECKED_CAST")
        return parameters.first { it?.metaData?.key == key } as FUN_P
    }

    protected fun <P : Parameter> add(parameter: P): P {
        val amountOfParameters = parameters.size
        check(nextParameterCursor < amountOfParameters) {
            "given amount of parameters is $amountOfParameters but current \"add\" call requires ${amountOfParameters + 1}"
        }
        parameters[nextParameterCursor++] = parameter
        return parameter
    }

    protected operator fun <P : Parameter> P.unaryPlus() = add(this)

    fun read(other: ParameterizedObject) {
        other.readIt {
            if (other.javaClass.isInstance(javaClass)) {
                //when other is instance of this class then layout of first parameters
                // is defined (offsets are equal) and can be read in O(n) time complexity
                val parameters = parameters
                val otherParameters = other.parameters
                for (i in parameters.indices) {
                   parameters[i]!!.readValueFromAnotherParameter(otherParameters[i]!!)
                }
            } else {
                //the layout is undefined otherwise and before every parameter's read need to find
                // the parameter that equals to current one and time complexity becomes to O(n^2)
                for (parameter in parameters) {
                   parameter!!
                   val otherParameter = other.get<Parameter>(parameter.metaData.key)
                   if (otherParameter !== null) {
                       parameter.readValueFromAnotherParameter(otherParameter)
                   }
                }
            }
        }
    }

    open fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, obj: OBJECT) {
        forEach { it.read(readCtx, readCtx.getElement(it.metaData.key, obj)) }
    }

    open fun <ELEMENT, OBJECT> write(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT) {
        forEach { writeCtx.writeElement(it.metaData.key, obj, it.toElement(writeCtx, obj)) }
    }

    override fun iterator() = object : Iterator<Parameter> {
        private var cursor = 0
        override fun hasNext() = cursor < parameters.size
        override fun next() = parameters[cursor++]!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ParameterizedObject
        return parameters.contentEquals(other.parameters)
    }

    override fun hashCode(): Int {
        val hashCode = parameters.hashCode()
        return hashCode
    }

    override fun toString(): String {
        return parameters.toString()
    }
}
