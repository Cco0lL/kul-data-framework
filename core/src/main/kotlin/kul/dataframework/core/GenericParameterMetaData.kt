package kul.dataframework.core

/**
 * @author Cco0lL created 11/29/24 4:11PM
 **/
abstract class GenericParameterMetaData<T>(
    key: String,
    prettyName: Any = "",
    description: List<Any> = emptyList()
): ParameterMetaData(key, prettyName, description) {

    abstract fun genericDefaultValue(ownerContainer: ParameterContainer<*>): T

    abstract fun <ELEMENT, OBJECT> fromElement(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
        param: GenericParameter<T>
    ): T

    abstract fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
        param: GenericParameter<T>
    ): ELEMENT
}