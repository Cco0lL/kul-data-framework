package kul.dataframework.core

/**
 * @author Cco0lL created 11/25/24 5:46AM
 **/
open class GenericParameter<T>(
    ownerContainer: ParameterContainer<*>,
    override val metaData: GenericParameterMetaData<T>,
    initialValue: T = metaData.genericDefaultValue(ownerContainer)
) : AbstractParameter(ownerContainer, metaData) {

    var value = initialValue
        set(value) {
            ownerContainer.checkIsModificationsEnabled()
            field = value
        }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = metaData.fromElement(readCtx, element, this)
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return metaData.toElement(writeCtx, obj, this)
    }

    operator fun getValue(thisRef: ParameterizedObject, property: Any?): T {
        allowSubscribe()
        return value
    }

    operator fun setValue(thisRef: ParameterizedObject, property: Any?, value: T) {
        this.value = value
        runSubscribers()
    }

    override fun copy(ownerContainer: ParameterContainer<*>): GenericParameter<T> =
        GenericParameter(ownerContainer, metaData, value)

    override fun toString() = "${metaData.prettyName}: $value"
}