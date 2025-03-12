package kul.dataframework.core

import java.util.ArrayList

/**
 * @author Cco0lL created 9/26/24 3:27AM
 **/
@Suppress("UNCHECKED_CAST")
class ParameterDictionary<P : Parameter, I : ParameterUniverseItem<P>>(
    universe: ParameterUniverse<P, I>,
) : ParameterCollection<P, I>(universe) {

    private var backingList = arrayListOf<Node>()

    override val size get() = backingList.size

    override fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P>): FUN_P? {
        return backingList.firstOrNull { it.key === universeItem } as? FUN_P
    }

    override fun add(universeItem: I, param: P) {
        backingList += Node(universeItem, param)
    }

    override fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P>): FUN_P? {
        val itr = backingList.iterator()
        while (itr.hasNext()) {
            val next = itr.next()
            if (next.key === universeItem) {
                itr.remove()
                return next as FUN_P
            }
        }
        return null
    }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        readCtx.elementAsCollection(
            element,
            { backingList },
            {
                val itemObject = readCtx.elementAsObject(it)
                val name = readCtx.readString("key", itemObject)

                val item = universe.getItemNonNull(name)
                val param = item.createParam()

                param.read(readCtx, readCtx.getElement("value", itemObject))

                Node(item, param)
            }
        )
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.collectionElement(backingList, obj) {
            val itemObject = writeCtx.createChildObject(obj)
            writeCtx.writeString("key", itemObject, it.key.name)
            writeCtx.writeElement("value", itemObject, it.value.toElement(writeCtx, itemObject))
            writeCtx.objectAsElement(itemObject)
        }
    }

    override fun clear() {
        backingList = ArrayList()
    }

    override fun copy(): ParameterDictionary<P, I> {
        return ParameterDictionary(universe).modifyIt { it.read(this) }
    }

    override fun iterator(): MutableIterator<Map.Entry<I, P>> {
        return backingList.iterator()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterDictionary<*, *>

        if (universe != other.universe) return false
        if (backingList != other.backingList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backingList.hashCode()
        result = result * 59 + universe.hashCode()
        return result
    }

    override fun toString(): String {
        return backingList.toString()
    }

    private inner class Node(
        override val key: I,
        override val value: P
    ) : Map.Entry<I, P> {
        override fun toString() = "{ \"$key\": \"$value\" }"
    }
}

@Suppress("UNCHECKED_CAST")
class ParameterMap<P : Parameter, I : ParameterUniverseItem<P>>(
    universe: ParameterUniverse<P, I>,
) : ParameterCollection<P, I>(universe) {

    private val backingMap = LinkedHashMap<I, P>()

    override val size get() = backingMap.size

    override fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P>): FUN_P? {
        return backingMap[universeItem as I] as? FUN_P
    }

    override fun add(universeItem: I, param: P) {
        backingMap[universeItem] = param
    }

    override fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P>): FUN_P? {
        return backingMap.remove(universeItem as I) as? FUN_P
    }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        readCtx.elementAsMap(
            element,
            { backingMap },
            { universe.getItemNonNull(readCtx.elementAsString(it)) },
            { k, e -> k.createParam().apply { read(readCtx, e) } }
        )
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.mapElement(
            backingMap, obj,
            { writeCtx.stringElement(it.name, obj) },
            { _, v -> v.toElement(writeCtx, obj) }
        )
    }

    override fun clear() { backingMap.clear() }

    override fun copy(): ParameterMap<P, I> {
        return ParameterMap(universe).modifyIt { it.read(this) }
    }

    override fun iterator(): MutableIterator<Map.Entry<I, P>> {
        return backingMap.entries.iterator()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterMap<*, *>

        if (backingMap != other.backingMap) return false
        if (universe != other.universe) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backingMap.hashCode()
        result = result * 59 + universe.hashCode()
        return result
    }

    override fun toString(): String {
        return backingMap.toString()
    }
}

@Suppress("UNCHECKED_CAST")
abstract class ParameterCollection<P : Parameter, I : ParameterUniverseItem<P>>(
    val universe: ParameterUniverse<P, I>,
) : ParameterContainer<P>(), MutableIterable<Map.Entry<I, P>> {

    abstract val size: Int

    override operator fun <FUN_P : P> get(key: String) = get(universe.getItemNonNull(key)) as? FUN_P
    abstract operator fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P>): FUN_P?

    abstract fun add(universeItem: I, param: P)
    fun <FUN_P : P> add(universeItem: ParameterUniverseItem<FUN_P>, initBlock: (FUN_P.() -> Unit)? = null) {
        val param = universeItem.createParam()
        initBlock?.run { param.apply(this) }
        add(universeItem as I, param)
    }

    operator fun minusAssign(universeItem: ParameterUniverseItem<P>) { remove(universeItem) }
    abstract fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P>): FUN_P?
    fun remove(key: String): P? { return remove(universe.getItemNonNull(key)) }

    open fun contains(key: String) = contains(universe.getItemNonNull(key))
    open fun contains(universeItem: ParameterUniverseItem<P>) =
        get(universeItem) !== null

    fun read(other: ParameterCollection<P, I>) {
        val universe = universe
        assert(universe == other.universe) {
            "can't read from collection, the universe of which is different"
        }
        clear()
        for ((item, param) in other) {
            val newParameter = item.createParam()
            newParameter.readValueFromAnotherParameter(param)
            add(item, newParameter)
        }
    }

    abstract fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    abstract fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT

    abstract fun clear()

    override fun copy(): ParameterCollection<P, I> {
        throw UnsupportedOperationException("Not Implemented")
    }
}