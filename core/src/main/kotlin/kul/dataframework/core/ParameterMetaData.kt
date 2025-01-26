package kul.dataframework.core

/**
 * @author Cco0lL created 11/24/24 12:09AM
 **/

/**
 * Represents metadata of parameter
 */
open class ParameterMetaData(
    /**
     * represents key of parameter. It is a good way
     * to set key that equals to property name, it will
     * help to avoid missunderstandings in serialization
     * context and data analysis.
     */
    val key: String,
    prettyName: Any = "",
    description: List<Any> = emptyList(),
) {
    /**
     * misc name for UI or UX or another case
     */
    val prettyName = prettyName.toString()
    /**
     * misc description for UI or UX or another case
     */
    val description = description.map { it.toString() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterMetaData

        if (key != other.key) return false
//        if (prettyName != other.prettyName) return false
//        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
//        result = 31 * result + prettyName.hashCode()
//        result = 31 * result + description.hashCode()
        return result
    }
}

open class EnumParameterMetadata<T : Enum<T>>(
    key: String,
    enumUniverse: Array<T>,
    prettyName: Any = "",
    description: List<Any> = emptyList()
) : ParameterMetaData(key, prettyName, description) {

    val enumUniverse = listOf(*enumUniverse)
}

open class CollectionParameterMetaData<P : Parameter, I : ParameterUniverseItem<out P, *>>(
    key: String,
    val parameterUniverse: ParameterUniverse<P, I>,
    prettyName: Any = "",
    description: List<Any> = emptyList()
): ParameterMetaData(key, prettyName, description)
