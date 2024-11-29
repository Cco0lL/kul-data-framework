package kul.dataframework.core

/**
 * @author Cco0lL created 10/9/24 12:16PM
 **/

/**
 * Represents a parameter, parameter is a basic unit of data-framework
 */
interface Parameter {

    val ownerContainer: ParameterContainer<*>
    val metaData: ParameterMetaData

    fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT
}