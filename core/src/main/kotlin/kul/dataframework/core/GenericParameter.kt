package kul.dataframework.core

import kotlin.reflect.KProperty

/**
 * @author Cco0lL created 11/25/24 5:46AM
 **/
abstract class GenericParameter<T>(initialValue: T) : Parameter() {

    open var value = initialValue

    open operator fun provideDelegate(thisRef: ParameterContainer<*>, property: KProperty<*>): GenericParameter<T> {
        if (thisRef !is ParameterizedObject) {
            throw IllegalArgumentException("can't be used as delegate of that property")
        }
        name = property.name
        thisRef._injectParameter(this)
        return this
    }

    open operator fun getValue(thisRef: ParameterContainer<*>, property: KProperty<*>): T {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: KProperty<*>, value: T) {
        this.value = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as GenericParameter<*>).value as T
    }

    override fun toString() = "${this::class.simpleName}: $value"
}