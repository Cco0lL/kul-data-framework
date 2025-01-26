package kul.dataframework.core

import java.util.*

/**
 * @author Cco0lL created 9/20/24 10:09PM
 **/
open class BooleanParameter(
    metaData: ParameterMetaData,
    initialValue: Boolean = false,
) : Parameter(metaData) {

    open var value = initialValue
        set(value) {
            if (!canModifyValue) {
                throw UnsupportedOperationException(messageIfModificationDisabled())
            }
            field = value
        }

    //FIXME: add boolean read and write methods in contexts
    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsInt(element) == 1
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.intElement(if (value) 1 else 0, obj)
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Boolean {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Boolean) {
        this.value = value
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
    metaData: ParameterMetaData,
    initialValue: Int = 0,
) : Parameter(metaData) {

    open var value = initialValue
        set(value) {
            if (!canModifyValue) {
                throw UnsupportedOperationException(messageIfModificationDisabled())
            }
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsInt(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.intElement(value, obj)
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Int {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Int) {
        this.value = value
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
    metaData: ParameterMetaData,
    initialValue: Float = 0f,
) : Parameter(metaData) {

    open var value = initialValue
        set(value) {
            if (!canModifyValue) {
                throw UnsupportedOperationException(messageIfModificationDisabled())
            }
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsFloat(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.floatElement(value, obj)
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Float {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Float) {
        this.value = value
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
    metaData: ParameterMetaData,
    initialValue: Long = 0L,
) : Parameter(metaData) {

    open var value = initialValue
        set(value) {
            if (!canModifyValue) {
                throw UnsupportedOperationException(messageIfModificationDisabled())
            }
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsLong(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.longElement(value, obj)
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Long {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Long) {
        this.value = value
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
    metaData: ParameterMetaData,
    initialValue: Double = 0.0,
) : Parameter(metaData) {

    open var value = initialValue
        set(value) {
            if (!canModifyValue) {
                throw UnsupportedOperationException(messageIfModificationDisabled())
            }
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsDouble(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.doubleElement(value, obj)
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): Double {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: Double) {
        this.value = value
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
    override val metaData: EnumParameterMetadata<T>,
    initialValue: T = metaData.enumUniverse[0],
) : GenericParameter<T>(metaData, initialValue) {

    val universe get() = metaData.enumUniverse

    override fun <ELEMENT, OBJECT> read(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
    ) {
        value = readCtx.elementAsEnum(element, metaData.enumUniverse)
    }

    override fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
    ): ELEMENT {
        return writeCtx.enumElement(value, obj)
    }

    fun set(name: String) {
        value = universe.first { it.name == name }
    }
}

open class StringParameter(
    override val metaData: ParameterMetaData,
    initialValue: String = "-",
): GenericParameter<String>(metaData, initialValue) {

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT, ) {
        value = readCtx.elementAsString(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT, ): ELEMENT {
        return writeCtx.stringElement(value, obj)
    }
}

open class ParameterizedObjectParameter<PO : ParameterizedObject>(
    override val metaData: ParameterMetaData,
    initialValue: PO
): Parameter(metaData) {

    override var canModifyValue: Boolean
        get() = super.canModifyValue
        set(value) {
            super.canModifyValue = value
            this.value.disableModifications()
        }

    var value = initialValue
        set(value) {
            if (!canModifyValue) {
                throw UnsupportedOperationException(messageIfModificationDisabled())
            }
            readValueFromAnotherObject(value)
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value.modify { read(readCtx, readCtx.elementAsObject(element)) }
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        val itemObject = writeCtx.createChildObject(obj)
        value.read { write(writeCtx, itemObject) }
        return writeCtx.objectAsElement(itemObject)
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): PO {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: PO) {
        this.value = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun readValueFromAnotherParameter(other: Parameter) {
        readValueFromAnotherObject((other as ParameterizedObjectParameter<PO>).value)
    }

    private fun readValueFromAnotherObject(other: PO) {
        value.modify { read(other) }
    }
}

open class CollectionParameter<P : Parameter, I : ParameterUniverseItem<out P, *>, C : ParameterCollection<P, I>>(
    override val metaData: CollectionParameterMetaData<P, I>,
    initialValue: C
) : Parameter(metaData) {

    override var canModifyValue: Boolean
        get() = super.canModifyValue
        set(value) {
            super.canModifyValue = value
            this.value.disableModifications()
        }

    var value = initialValue
        set(value) {
            if (!canModifyValue) {
                throw UnsupportedOperationException(messageIfModificationDisabled())
            }
            readValueFromAnotherCollection(value)
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value.modify { read(readCtx, element) }
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        val collectionObject = writeCtx.createChildObject(obj)
        return value.read { toElement(writeCtx, collectionObject) }
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): C {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: C) {
        this.value = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun readValueFromAnotherParameter(other: Parameter) {
        readValueFromAnotherCollection((other as CollectionParameter<P, I, C>).value)
    }

    private fun readValueFromAnotherCollection(other: C) {
        value.modify { read(other) }
    }
}

open class DictionaryParameter<P : Parameter, I : ParameterUniverseItem<out P, *>>(
    metaData: CollectionParameterMetaData<P, I>,
    initialValue: ParameterDictionary<P, I> = ParameterDictionary(metaData.parameterUniverse)
): CollectionParameter<P, I, ParameterDictionary<P, I>>(metaData, initialValue)

open class MapParameter<P : Parameter, I : ParameterUniverseItem<out P, *>>(
    metaData: CollectionParameterMetaData<P, I>,
    initialValue: ParameterMap<P, I> = ParameterMap(metaData.parameterUniverse)
): CollectionParameter<P, I, ParameterMap<P, I>>(metaData, initialValue)

open class UUIDParameter(
    metaData: ParameterMetaData,
    initialValue: UUID = ZERO_VALUE
): GenericParameter<UUID>(metaData, initialValue) {

    override fun <ELEMENT, OBJECT> read(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
    ) {
        val asObject = readCtx.elementAsObject(element)
        val most = readCtx.readLong("most", asObject)
        val least = readCtx.readLong("least", asObject)
        value = if (most == 0L && least == 0L) ZERO_VALUE else UUID(most, least)
    }

    override fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
    ): ELEMENT {
        val value = this.value
        val objectOf = writeCtx.createChildObject(obj)
        writeCtx.writeLong("most", objectOf, value.mostSignificantBits)
        writeCtx.writeLong("least", objectOf, value.leastSignificantBits)
        return writeCtx.objectAsElement(objectOf)
    }

    companion object {
        val ZERO_VALUE = UUID(0, 0)
    }
}