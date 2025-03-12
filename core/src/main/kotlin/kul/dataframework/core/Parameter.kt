package kul.dataframework.core

/**
 * @author Cco0lL created 9/16/24 3:57PM
 **/

abstract class Parameter {

    lateinit var name: String
        internal set

    abstract fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    abstract fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT

    abstract fun readValueFromAnotherParameter(other: Parameter)
}