package kul.dataframework.core

/**
 * @author Cco0lL created 9/20/24 10:09PM
 **/
open class BooleanParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Boolean = false
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

    operator fun getValue(thisRef: ParameterizedObject, property: Any?): Boolean {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterizedObject, property: Any?, value: Boolean) {
        this.value = value
        runSubscribers()
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

    operator fun getValue(thisRef: ParameterizedObject, property: Any?): Int {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterizedObject, property: Any?, value: Int) {
        this.value = value
        runSubscribers()
    }
}

open class FloatParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Float = 0f
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

    operator fun getValue(thisRef: ParameterizedObject, property: Any?): Float {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterizedObject, property: Any?, value: Float) {
        this.value = value
        runSubscribers()
    }
}

open class LongParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Long = 0L
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

    operator fun getValue(thisRef: ParameterizedObject, property: Any?): Long {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterizedObject, property: Any?, value: Long) {
        this.value = value
        runSubscribers()
    }
}

open class DoubleParameter(
    ownerContainer: ParameterContainer<*>,
    metaData: ParameterMetaData,
    initialValue: Double = 0.0
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

    operator fun getValue(thisRef: ParameterizedObject, property: Any?): Double {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterizedObject, property: Any?, value: Double) {
        this.value = value
        runSubscribers()
    }
}

open class EnumParameter<T : Enum<T>>(
    ownerContainer: ParameterContainer<*>,
    override val metaData: EnumParameterMetadata<T>,
    initialValue: T = metaData.enumUniverse[0]
) : GenericParameter<T>(ownerContainer, metaData, initialValue) {

    val universe get() = metaData.enumUniverse

    fun set(name: String) {
        value = universe.first { it.name == name }
    }
}