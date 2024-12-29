package kul.dataframework.core

/**
 * @author Cco0lL created 9/20/24 10:09PM
 **/
open class BooleanParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Boolean = false,
) : AbstractParameter(ownerContainer, metaData) {

    var value = initialValue
        set(value) {
            ownerContainer.checkIsModificationsEnabled()
            field = value
        }

    //FIXME: add boolean read and write methods in contexts
    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsInt(element) == 1
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.intElement(if (value) 1 else 0, obj)
    }

    operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Boolean {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Boolean) {
        this.value = value
        runSubscribers()
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as BooleanParameter).value
    }

    override fun toString() = "${metaData.key}: $value"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BooleanParameter

        if (metaData != other.metaData) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metaData.hashCode()
        result = result * 59 + value.hashCode()
        return result
    }
}

open class IntParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Int = 0,
) : AbstractParameter(ownerContainer, metaData) {

    var value = initialValue
        set(value) {
            ownerContainer.checkIsModificationsEnabled()
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsInt(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.intElement(value, obj)
    }

    operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Int {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Int) {
        this.value = value
        runSubscribers()
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as IntParameter).value
    }

    override fun toString() = "${metaData.key}: $value"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntParameter

        if (metaData != other.metaData) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metaData.hashCode()
        result = result * 59 + value.hashCode()
        return result
    }
}

open class FloatParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Float = 0f,
) : AbstractParameter(ownerContainer, metaData) {

    var value = initialValue
        set(value) {
            ownerContainer.checkIsModificationsEnabled()
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsFloat(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.floatElement(value, obj)
    }

    operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Float {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Float) {
        this.value = value
        runSubscribers()
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as FloatParameter).value
    }

    override fun toString() = "${metaData.key}: $value"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatParameter

        if (metaData != other.metaData) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metaData.hashCode()
        result = result * 59 + value.hashCode()
        return result
    }
}

open class LongParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Long = 0L,
) : AbstractParameter(ownerContainer, metaData) {

    var value = initialValue
        set(value) {
            ownerContainer.checkIsModificationsEnabled()
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsLong(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.longElement(value, obj)
    }

    operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Long {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Long) {
        this.value = value
        runSubscribers()
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as LongParameter).value
    }

    override fun toString() = "${metaData.prettyName}: $value"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LongParameter

        if (metaData != other.metaData) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metaData.hashCode()
        result = result * 59 + value.hashCode()
        return result
    }
}

open class DoubleParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Double = 0.0,
) : AbstractParameter(ownerContainer, metaData) {

    var value = initialValue
        set(value) {
            ownerContainer.checkIsModificationsEnabled()
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsDouble(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.doubleElement(value, obj)
    }

    operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Double {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Double) {
        this.value = value
        runSubscribers()
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as DoubleParameter).value
    }

    override fun toString() = "${metaData.prettyName}: $value"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DoubleParameter

        if (metaData != other.metaData) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metaData.hashCode()
        result = result * 59 + value.hashCode()
        return result
    }
}

open class EnumParameter<T : Enum<T>>(
    ownerContainer: ParameterContainer<*>,
    override val metaData: EnumParameterMetadata<T>,
    initialValue: T = metaData.enumUniverse[0],
) : GenericParameter<T>(ownerContainer, metaData, initialValue) {

    val universe get() = metaData.enumUniverse

    fun set(name: String) {
        value = universe.first { it.name == name }
    }
}

open class StringParameter(
    ownerContainer: ParameterContainer<*>,
    override val metaData: StringParameterMetadata,
): GenericParameter<String>(ownerContainer, metaData, metaData.genericDefaultValue(ownerContainer))

open class ParameterizedObjectParameter<PO : ParameterizedObject>(
    ownerContainer: ParameterContainer<*>,
    override val metaData: ParameterizedObjectParameterMetaData<PO, *>
): GenericParameter<PO>(ownerContainer, metaData, metaData.genericDefaultValue(ownerContainer)) {

    @Suppress("UNCHECKED_CAST")
    override fun readValueFromAnotherParameter(other: Parameter) {
        val anotherParamObject = (other as ParameterizedObjectParameter<PO>).value
        readAnotherParameterizedObject(anotherParamObject)
    }

    override fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: PO) {
        readAnotherParameterizedObject(value)
        runSubscribers()
    }

    private fun readAnotherParameterizedObject(po: PO) {
        value.modify {
            for (param in po) {
                get(param.metaData.key)!!.readValueFromAnotherParameter(param)
            }
        }
    }
}

open class CollectionParameter<P : Parameter, C : ParameterCollection<P>>(
    ownerContainer: ParameterContainer<*>,
    override val metaData: CollectionParameterMetaData<P, C, *>,
) : GenericParameter<C>(ownerContainer, metaData, metaData.genericDefaultValue(ownerContainer)) {

    @Suppress("UNCHECKED_CAST")
    override fun readValueFromAnotherParameter(other: Parameter) {
        readAnotherParameterCollection((other as CollectionParameter<P, C>).value)
    }

    override fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: C) {
        readAnotherParameterCollection(value)
    }

    private fun readAnotherParameterCollection(c: C) {
        value.modify {
            clear(desiredSize = c.size)
            val universe = universe
            for (param in c) {
                val copyParam = universe.create(param.metaData.key, this)
                copyParam.readValueFromAnotherParameter(copyParam)
                this += copyParam
            }
        }
    }
}