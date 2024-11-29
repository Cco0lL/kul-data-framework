@file:Suppress("UNCHECKED_CAST")

package kul.dataframework.core

import java.util.*

/**
 * @author Cco0lL created 11/29/24 4:24PM
 **/
open class StringParameterMetadata(
    key: String,
    prettyName: Any = "",
    description: List<Any> = emptyList()
): GenericParameterMetaData<String>(key, prettyName, description) {

    override fun genericDefaultValue(ownerContainer: ParameterContainer<*>): String {
        return "-"
    }

    override fun <ELEMENT, OBJECT> fromElement(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
        param: GenericParameter<String>
    ): String {
        return readCtx.elementAsString(element)
    }

    override fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
        param: GenericParameter<String>
    ): ELEMENT {
        return writeCtx.stringElement(param.value, obj)
    }
}

open class EnumParameterMetadata<T : Enum<T>>(
    key: String,
    enumUniverse: Array<T>,
    prettyName: Any = "",
    description: List<Any> = emptyList()
) : GenericParameterMetaData<T>(key, prettyName, description) {
    val enumUniverse = listOf(*enumUniverse)

    override fun genericDefaultValue(ownerContainer: ParameterContainer<*>): T = enumUniverse[0]

    override fun <ELEMENT, OBJECT> fromElement(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
        param: GenericParameter<T>
    ): T {
        return readCtx.elementAsEnum(element, enumUniverse)
    }

    override fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
        param: GenericParameter<T>,
    ): ELEMENT {
        return writeCtx.enumElement(param.value, obj)
    }
}

open class ContentItemParameterMetaData<ITEM : ParameterizedObject, ROOT : ParameterContainer<*>>(
    key: String,
    val contentItemFactory: (ROOT) -> ITEM,
    prettyName: Any = "",
    description: List<Any> = emptyList()
) : GenericParameterMetaData<ITEM>(key, prettyName, description) {

    override fun genericDefaultValue(ownerContainer: ParameterContainer<*>) =
        contentItemFactory(ownerContainer as ROOT)

    override fun <ELEMENT, OBJECT> fromElement(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
        param: GenericParameter<ITEM>
    ): ITEM {
        return genericDefaultValue(param.ownerContainer)
            .modify { read(readCtx, readCtx.elementAsObject(element)) }
    }

    override fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
        param: GenericParameter<ITEM>
    ): ELEMENT {
        val itemObject = writeCtx.createChildObject(obj)
        param.value.acquireRead { write(writeCtx, itemObject) }
        return writeCtx.objectAsElement(itemObject)
    }
}

open class CollectionParameterMetaData<P : Parameter, C : ParameterCollection<P>, ROOT : ParameterContainer<*>>(
    key: String,
    val parameterUniverse: ParameterUniverse<P>,
    val collectionFactory: (ROOT, ParameterUniverse<P>) -> C,
    prettyName: Any = "",
    description: List<Any> = emptyList()
) : GenericParameterMetaData<C>(key, prettyName, description) {
    //FIXME: remove ad hoc if not handy

    override fun genericDefaultValue(ownerContainer: ParameterContainer<*>) =
        collectionFactory(ownerContainer as ROOT, parameterUniverse)

    override fun <ELEMENT, OBJECT> fromElement(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
        param: GenericParameter<C>
    ): C {
        return genericDefaultValue(param.ownerContainer)
            .modify { read(readCtx, element) }
    }

    override fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
        param: GenericParameter<C>
    ): ELEMENT {
        return param.value.acquireRead { toElement(writeCtx, obj) }
    }
}

open class UUIDParameterMetaData(
    key: String,
    prettyName: Any = "",
    description: List<Any> = emptyList()
) : GenericParameterMetaData<UUID>(key, prettyName, description) {

    override fun genericDefaultValue(ownerContainer: ParameterContainer<*>): UUID {
        return ZERO_VALUE
    }

    override fun <ELEMENT, OBJECT> fromElement(
        readCtx: ReadContext<ELEMENT, OBJECT>,
        element: ELEMENT,
        param: GenericParameter<UUID>
    ): UUID {
        val asObject = readCtx.elementAsObject(element)
        val most = readCtx.readLong("most", asObject)
        val least = readCtx.readLong("least", asObject)
        return if (most == 0L && least == 0L) ZERO_VALUE else UUID(most, least)
    }

    override fun <ELEMENT, OBJECT> toElement(
        writeCtx: WriteContext<ELEMENT, OBJECT>,
        obj: OBJECT,
        param: GenericParameter<UUID>
    ): ELEMENT {
        val value = param.value
        val objectOf = writeCtx.createChildObject(obj)
        writeCtx.writeLong("most", objectOf, value.mostSignificantBits)
        writeCtx.writeLong("least", objectOf, value.leastSignificantBits)
        return writeCtx.objectAsElement(objectOf)
    }

    companion object {
        val ZERO_VALUE = UUID(0, 0)
    }
}
