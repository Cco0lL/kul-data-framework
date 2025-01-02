package kul.dataframework.core

/**
 * @author Cco0lL created 11/25/24 5:46AM
 **/
abstract class GenericParameter<T>(
    override val metaData: ParameterMetaData,
    initialValue: T
) : Parameter(metaData) {

    open var value = initialValue

    open operator fun getValue(thisRef: ParameterContainer<*>, property: Any?): T {
        return value
    }

    open operator fun setValue(thisRef: ParameterContainer<*>, property: Any?, value: T) {
        this.value = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun readValueFromAnotherParameter(other: Parameter) {
        value = (other as GenericParameter<*>).value as T
    }

    override fun toString() = "${metaData.key}: $value"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenericParameter<*>

        if (metaData != other.metaData) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metaData.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}