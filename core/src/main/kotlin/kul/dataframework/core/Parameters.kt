package kul.dataframework.core

import java.util.*
import kotlin.enums.EnumEntries
import kotlin.reflect.KProperty

/**
 * @author Cco0lL created 9/20/24 10:09PM
 **/
open class BooleanParameter(initialValue: Boolean = false) : Parameter() {

    open var value = initialValue

    //FIXME: add boolean read and write methods in contexts
    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsInt(element) == 1
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.intElement(if (value) 1 else 0, obj)
    }

    open operator fun provideDelegate(thisRef: ParameterContainer<*>, property: KProperty<*>): BooleanParameter {
        if (thisRef !is ParameterizedObject) {
            throw IllegalArgumentException("can't be used as delegate of that property")
        }
        name = property.name
        thisRef._injectParameter(this)
        return this
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: KProperty<*>): Boolean {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: KProperty<*>, value: Boolean) {
        this.value = value
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as BooleanParameter).value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BooleanParameter

        return name == other.name && value == other.value
    }

    override fun hashCode(): Int {
        var hashCode = name.hashCode()
        hashCode = 31 * hashCode + value.hashCode()
        return hashCode
    }

    override fun toString() = "$name: $value"
}

open class IntParameter(initialValue: Int = 0) : Parameter() {

    open var value = initialValue

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsInt(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.intElement(value, obj)
    }

    open operator fun provideDelegate(thisRef: ParameterContainer<*>, property: KProperty<*>): IntParameter {
        if (thisRef !is ParameterizedObject) {
            throw IllegalArgumentException("can't be used as delegate of that property")
        }
        name = property.name
        thisRef._injectParameter(this)
        return this
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: KProperty<*>): Int {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: KProperty<*>, value: Int) {
        this.value = value
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as IntParameter).value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntParameter

        return name == other.name && value == other.value
    }

    override fun hashCode(): Int {
        var hashCode = name.hashCode()
        hashCode = 31 * hashCode + value.hashCode()
        return hashCode
    }

    override fun toString() = "$name: $value"
}

open class FloatParameter(initialValue: Float = 0f) : Parameter() {

    open var value = initialValue

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsFloat(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.floatElement(value, obj)
    }

    open operator fun provideDelegate(thisRef: ParameterContainer<*>, property: KProperty<*>): FloatParameter {
        if (thisRef !is ParameterizedObject) {
            throw IllegalArgumentException("can't be used as delegate of that property")
        }
        name = property.name
        thisRef._injectParameter(this)
        return this
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: KProperty<*>): Float {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: KProperty<*>, value: Float) {
        this.value = value
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as FloatParameter).value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatParameter

        return name == other.name && value == other.value
    }

    override fun hashCode(): Int {
        var hashCode = name.hashCode()
        hashCode = 31 * hashCode + value.hashCode()
        return hashCode
    }

    override fun toString() = "$name: $value"
}

open class LongParameter(initialValue: Long = 0L) : Parameter() {

    open var value = initialValue

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsLong(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.longElement(value, obj)
    }

    open operator fun provideDelegate(thisRef: ParameterContainer<*>, property: KProperty<*>): LongParameter {
        if (thisRef !is ParameterizedObject) {
            throw IllegalArgumentException("can't be used as delegate of that property")
        }
        name = property.name
        thisRef._injectParameter(this)
        return this
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: KProperty<*>): Long {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: KProperty<*>, value: Long) {
        this.value = value
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as LongParameter).value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LongParameter

        return name == other.name && value == other.value
    }

    override fun hashCode(): Int {
        var hashCode = name.hashCode()
        hashCode = 31 * hashCode + value.hashCode()
        return hashCode
    }

    override fun toString() = "$name: $value"
}

open class DoubleParameter(initialValue: Double = 0.0): Parameter() {

    open var value = initialValue

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsDouble(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.doubleElement(value, obj)
    }

    open operator fun provideDelegate(thisRef: ParameterContainer<*>, property: KProperty<*>): DoubleParameter {
        if (thisRef !is ParameterizedObject) {
            throw IllegalArgumentException("can't be used as delegate of that property")
        }
        name = property.name
        thisRef._injectParameter(this)
        return this
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: KProperty<*>): Double {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: KProperty<*>, value: Double) {
        this.value = value
    }

    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as DoubleParameter).value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DoubleParameter

        return name == other.name && value == other.value
    }

    override fun hashCode(): Int {
        var hashCode = name.hashCode()
        hashCode = 31 * hashCode + value.hashCode()
        return hashCode
    }

    override fun toString() = "$name: $value"
}

open class EnumParameter<T : Enum<T>>(
    val enumEntries: EnumEntries<T>,
    initialValue: T = enumEntries[0],
) : GenericParameter<T>(initialValue) {

    override fun <ELEMENT, OBJECT> read(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
    ) {
        value = readCtx.elementAsEnum(element, enumEntries)
    }

    override fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
    ): ELEMENT {
        return writeCtx.enumElement(value, obj)
    }

    fun set(name: String) {
        value = enumEntries.first { it.name == name }
    }
}

open class StringParameter(initialValue: String = "-"): GenericParameter<String>(initialValue) {

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = readCtx.elementAsString(element)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.stringElement(value, obj)
    }
}

open class ParameterizedObjectParameter<PO : ParameterizedObject>(initialValue: PO): Parameter() {

    open var value = initialValue
        set(value) {
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

    open operator fun provideDelegate(thisRef: ParameterContainer<*>, property: KProperty<*>): ParameterizedObjectParameter<PO> {
        if (thisRef !is ParameterizedObject) {
            throw IllegalArgumentException("can't be used as delegate of that property")
        }
        name = property.name
        thisRef._injectParameter(this)
        return this
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: KProperty<*>): PO {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: KProperty<*>, value: PO) {
        this.value = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun readValueFromAnotherParameter(other: Parameter) {
        readValueFromAnotherObject((other as ParameterizedObjectParameter<PO>).value)
    }

    private fun readValueFromAnotherObject(other: PO) {
        value.modify { read(other) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterizedObjectParameter<*>

        return name == other.name && value == other.value
    }

    override fun hashCode(): Int {
        var hashCode = name.hashCode()
        hashCode = 31 * hashCode + value.hashCode()
        return hashCode
    }

    override fun toString() = "$name: $value"

}

open class ParameterCollectionParameter<P : Parameter, I : ParameterUniverseItem<P>, C : ParameterCollection<P, I>>(
    val parameterUniverse: ParameterUniverse<P, *>,
    initialValue: C
) : Parameter() {

    open var value = initialValue
        set(value) {
            readValueFromAnotherCollection(value)
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value.modify { read(readCtx, element) }
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        val collectionObject = writeCtx.createChildObject(obj)
        return value.read { toElement(writeCtx, collectionObject) }
    }

    open operator fun provideDelegate(thisRef: ParameterContainer<*>, property: KProperty<*>): ParameterCollectionParameter<P, I, C> {
        if (thisRef !is ParameterizedObject) {
            throw IllegalArgumentException("can't be used as delegate of that property")
        }
        name = property.name
        thisRef._injectParameter(this)
        return this
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: KProperty<*>): C {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: KProperty<*>, value: C) {
        this.value = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun readValueFromAnotherParameter(other: Parameter) {
        readValueFromAnotherCollection((other as ParameterCollectionParameter<P, I, C>).value)
    }

    private fun readValueFromAnotherCollection(other: C) {
        value.modify { read(other) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterCollectionParameter<*, *, *>

        return name == other.name && value == other.value
    }

    override fun hashCode(): Int {
        var hashCode = name.hashCode()
        hashCode = 31 * hashCode + value.hashCode()
        return hashCode
    }

    override fun toString() = "$name: $value"
}

open class DictionaryParameter<P : Parameter, I : ParameterUniverseItem<P>>(
    parameterUniverse: ParameterUniverse<P, I>,
    initialValue: ParameterDictionary<P, I> = ParameterDictionary(parameterUniverse)
): ParameterCollectionParameter<P, I, ParameterDictionary<P, I>>(parameterUniverse, initialValue)

open class MapParameter<P : Parameter, I : ParameterUniverseItem<P>>(
    parameterUniverse: ParameterUniverse<P, I>,
    initialValue: ParameterMap<P, I> = ParameterMap(parameterUniverse)
): ParameterCollectionParameter<P, I, ParameterMap<P, I>>(parameterUniverse, initialValue)

open class UUIDParameter(initialValue: UUID = ZERO_VALUE): GenericParameter<UUID>(initialValue) {

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