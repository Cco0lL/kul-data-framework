package kul.dataframework.core

/**
 * @author Cco0lL created 11/24/24 12:09AM
 **/

/**
 * Represents metadata of parameter
 */
open class ParameterMetaData(
    /**
     * represents key of parameter. It is a good practice
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
}