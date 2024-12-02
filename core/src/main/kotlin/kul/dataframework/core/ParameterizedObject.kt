package kul.dataframework.core

/**
 * @author Cco0lL created 9/21/24 5:14PM
 **/

open class ParameterizedObject(
    rootContainer: ParameterContainer<*>? = null
) : ParameterContainer<Parameter>(rootContainer) {

    val parameterMap = mutableMapOf<String, Parameter>()

    override fun get(name: String): Parameter {
        return parameterMap[name]!!
    }

    override fun iterator() = object : Iterator<Parameter> {
        private val backingParamSchemeItr = parameterMap.values.iterator()
        override fun hasNext() = backingParamSchemeItr.hasNext()
        override fun next() = backingParamSchemeItr.next()
    }

    open fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, obj: OBJECT) {
        checkIsModificationsEnabled()
        forEach { it.read(readCtx, readCtx.getElement(it.metaData.key, obj)) }
    }

    open fun <ELEMENT, OBJECT> write(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT) {
        forEach { writeCtx.writeElement(it.metaData.key, obj, it.toElement(writeCtx, obj)) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ParameterizedObject
        return parameterMap == other.parameterMap
    }

    override fun hashCode(): Int {
        val hashCode = parameterMap.hashCode()
        return hashCode
    }

    override fun toString(): String {
        return parameterMap.values.toString()
    }

    // use bottom functions for initialization only
    protected fun booleanParam(
        metaData: ParameterMetaData,
        initialValue: Boolean = false
    ) = +BooleanParameter(ownerContainerForParameter, metaData, initialValue)

    protected fun intParam(
        metaData: ParameterMetaData,
        initialValue: Int = 0,
    ) = +IntParameter(ownerContainerForParameter, metaData, initialValue)

    protected fun floatParam(
        metaData: ParameterMetaData,
        initialValue: Float = 0f
    ) =  +FloatParameter(ownerContainerForParameter, metaData, initialValue)

    protected fun longParam(
        metaData: ParameterMetaData,
        initialValue: Long = 0L
    ) = +LongParameter(ownerContainerForParameter, metaData, initialValue)

    protected fun doubleParam(
        metaData: ParameterMetaData,
        initialValue: Double = 0.0
    ) = +DoubleParameter(ownerContainerForParameter, metaData, initialValue)

    protected fun <T> param(
        metaData: GenericParameterMetaData<T>,
        initialValue: T = metaData.genericDefaultValue(ownerContainerForParameter)
    ) = +GenericParameter(ownerContainerForParameter, metaData, initialValue)

    protected fun <T : Enum<T>> enumParam(
        metaData: EnumParameterMetadata<T>,
        initialValue: T = metaData.genericDefaultValue(ownerContainerForParameter),
    ) = +EnumParameter(ownerContainerForParameter, metaData, initialValue)

    // opened for cases when you need create explicit parameter function like an enumParam,
    // the best way to do that is extensions
    val ownerContainerForParameter get() = rootContainer ?: this

    private operator fun <P : Parameter> P.unaryPlus() = apply {
        parameterMap[metaData.key] = this
    }
}