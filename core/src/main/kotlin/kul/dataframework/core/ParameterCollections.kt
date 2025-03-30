package kul.dataframework.core

import java.util.ArrayList

/**
 * @author Cco0lL created 9/26/24 3:27AM
 **/
@Suppress("UNCHECKED_CAST")
class ParameterDictionary<P : Parameter, I : ParameterUniverseItem<P>>(
    universe: ParameterUniverse<P, I>, private val ordinalOrder: Boolean = false
) : ParameterCollection<P, I>(universe) {

    private var backingList = arrayListOf<Node>()

    override val size get() = backingList.size

    override fun clear() {
        backingList = ArrayList()
    }

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> getImpl(item: FUN_I): P? {
        val size = size
        if (size == 0) {
            return null
        }

        if (size == 1) {
            val p1 = backingList[0]
            if (p1.key === item)
                return p1.value
            return null
        }

        if (ordinalOrder) {
            val i = backingList.binarySearchBy(item.ordinal) { it.key.ordinal }
            if (i > 0)
                return backingList[i].value
            return null
        }

        return backingList.firstOrNull { it.key === item }?.value
    }

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> setImpl(item: FUN_I, param: FUN_P) {
        val node = Node(item as I, param as P)
        if (ordinalOrder && size > 0) {
            var insertionPoint = backingList.binarySearchBy(item.ordinal) { it.key.ordinal }

            if (insertionPoint < 0)
                insertionPoint = -(insertionPoint + 1)

            backingList.add(insertionPoint, node)

            return
        }
        backingList += node
    }

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> removeImpl(item: FUN_I): P? {
        val size = size
        if (size == 0) {
            return null
        }

        if (size == 1) {
            val p1 = backingList[0]
            if (p1.key === item)
                return backingList.removeAt(0).value
            return null
        }

        if (ordinalOrder) {
            val i = backingList.binarySearchBy(item.ordinal) { it.key.ordinal }
            if (i > 0)
                return backingList.removeAt(i).value
            return null
        }

        val itr = backingList.iterator()
        while (itr.hasNext()) {
            val next = itr.next()
            if (next.key === item) {
                itr.remove()
                return next.value
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

                val item = universe.getItemByNameNonNull(name)
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
        override var value: P
    ) : Map.Entry<I, P> {
        override fun toString() = "{ \"$key\": \"$value\" }"
    }
}

@Suppress("UNCHECKED_CAST")
class ParameterMap<P : Parameter, I : ParameterUniverseItem<P>>(
    universe: ParameterUniverse<P, I>,
    backingMapSupplier: () -> MutableMap<I, P> = { hashMapOf() }
) : ParameterCollection<P, I>(universe) {

    private val backingMap = backingMapSupplier()

    override val size get() = backingMap.size

    override fun clear() { backingMap.clear() }

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> getImpl(item: FUN_I) = backingMap[item as I]

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> setImpl(item: FUN_I, param: FUN_P) {
        backingMap[item as I] = param as P
    }

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> removeImpl(item: FUN_I): P? {
        return backingMap.remove(item as I)
    }

    override fun copy(): ParameterMap<P, I> {
        return ParameterMap(universe).modifyIt { it.read(this) }
    }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        readCtx.elementAsMap(
            element,
            { backingMap },
            { universe.getItemByNameNonNull(readCtx.elementAsString(it)) },
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

    override operator fun <FUN_P : P> get(key: String): FUN_P? {
        return getImpl(universe.getItemByNameNonNull(key)) as? FUN_P
    }

    operator fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> get(item: FUN_I): FUN_P? {
        assertItemIsPresentedInUniverse(item)
        return getImpl(item) as? FUN_P
    }

    operator fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> set(item: FUN_I, param: FUN_P) {
        assertItemIsPresentedInUniverse(item)
        setImpl(item, param)
    }

    operator fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> invoke(item: FUN_I, initBlock: FUN_P.() -> Unit) {
        assertItemIsPresentedInUniverse(item)
        val param = item.createParam()
        initBlock(param)
        setImpl(item, param)
    }

    fun <FUN_P : Parameter> remove(item: ParameterUniverseItem<FUN_P>): FUN_P? {
        assertItemIsPresentedInUniverse(item)
        return removeImpl(item) as? FUN_P?
    }

    fun remove(key: String): P? {
        return remove(universe.getItemByNameNonNull(key))
    }

    open fun contains(key: String) = contains(universe.getItemByNameNonNull(key))
    open fun contains(universeItem: ParameterUniverseItem<P>) = get(universeItem) !== null

    private fun assertItemIsPresentedInUniverse(item: ParameterUniverseItem<*>) {
        assert(universe.getItemByOrdinal(item.ordinal) === item) { "$item is not presented in $universe" }
    }

    fun read(other: ParameterCollection<P, I>) {
        val universe = universe
        assert(universe === other.universe) {
            "can't read from collection, the universe of which is different"
        }
        clear()
        for ((item, param) in other) {
            val newParameter = item.createParam()
            newParameter.readValueFromAnotherParameter(param)
            setImpl(item, newParameter)
        }
    }

    abstract val size: Int

    abstract fun clear()

    protected abstract fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> getImpl(item: FUN_I): P?
    protected abstract fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> setImpl(item: FUN_I, param: FUN_P)
    protected abstract fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> removeImpl(item: FUN_I): P?

    abstract fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    abstract fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT

    override fun copy(): ParameterCollection<P, I> {
        throw UnsupportedOperationException("Not Implemented")
    }
}