package kul.dataframework.core

/**
 * @author Cco0lL created 9/16/24 3:57PM
 **/
abstract class Parameter(
    open val metaData: ParameterMetaData,
) {

    open var canModifyValue = true

    open fun messageIfModificationDisabled() =
        "modifications are disabled for parameter with key \"${metaData.key}\". Set canModifyValue = true" +
                " for parameter to make sure it wasn't mistake"

    abstract fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    abstract fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT

    abstract fun readValueFromAnotherParameter(other: Parameter)

    override fun toString() = "parameter { class: ${this::class.simpleName}, name: ${metaData.key} }"
}