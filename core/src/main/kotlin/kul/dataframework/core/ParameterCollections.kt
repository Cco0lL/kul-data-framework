package kul.dataframework.core

import java.util.*

/**
 * @author Cco0lL created 9/26/24 3:27AM
 **/
@Suppress("UNCHECKED_CAST")
class ParameterDictionary<P : Parameter, I : ParameterUniverseItem<P>>(
    universe: ParameterUniverse<P, I>,
    capacity: Int = DEFAULT_CAPACITY,
    private val ordinalOrder: Boolean = false
) : ParameterCollection<P, I>(universe) {

    private var backingList: MutableList<Node<I, P>>
    init {
        backingList = if (capacity == DEFAULT_CAPACITY)
            Collections.emptyList()
        else
            ArrayList(capacity)
    }

    override val size get() = backingList.size

    override fun clear() {
        backingList = Collections.emptyList()
    }

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> getImpl(item: FUN_I): P? {
        val size = size
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

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> putImpl(item: FUN_I, param: FUN_P) {
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
        backingList = readCtx.elementAsCollection(
            element,
            { ArrayList(it) },
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

    override fun isNotAllocated(): Boolean {
        return backingList === Collections.EMPTY_LIST
    }

    override fun allocate() {
        backingList = ArrayList(DEFAULT_CAPACITY)
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

    private class Node<I, P>(
        override val key: I,
        override var value: P
    ) : Map.Entry<I, P> {

        override fun toString() = "{ \"$key\": \"$value\" }"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Node<*, *>

            if (key != other.key) return false
            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            var result = key.hashCode()
            result = 31 * result + value.hashCode()
            return result
        }
    }

    companion object {
        private const val DEFAULT_CAPACITY = 10
    }
}

fun interface MapCreator<K, V> {
    fun create(capacity: Int, loadFactor: Float): MutableMap<K, V>
}

@Suppress("UNCHECKED_CAST")
class ParameterMap<P : Parameter, I : ParameterUniverseItem<P>>(
    universe: ParameterUniverse<P, I>,
    capacity: Int = DEFAULT_CAPACITY,
    loadFactor: Float = DEFAULT_LOAD_FACTOR,
    private val backingMapCreator: MapCreator<I, P> = MapCreator { cp: Int, lf: Float ->
        HashMap(((cp / lf) + 1).toInt(), lf)
    }
) : ParameterCollection<P, I>(universe) {

    private var backingMap: MutableMap<I, P>
    init {
        backingMap = if (capacity == DEFAULT_CAPACITY && loadFactor == DEFAULT_LOAD_FACTOR) {
            Collections.emptyMap()
        } else {
            backingMapCreator.create(capacity, loadFactor)
        }
    }

    override val size get() = backingMap.size

    override fun clear() { backingMap.clear() }

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> getImpl(item: FUN_I) = backingMap[item as I]

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> putImpl(item: FUN_I, param: FUN_P) {
        backingMap[item as I] = param as P
    }

    override fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> removeImpl(item: FUN_I): P? {
        return backingMap.remove(item as I)
    }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        backingMap = readCtx.elementAsMap(
            element,
            { backingMapCreator.create(it, DEFAULT_LOAD_FACTOR) },
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

    override fun isNotAllocated(): Boolean {
        return backingMap === Collections.EMPTY_MAP
    }

    override fun allocate() {
        backingMap = backingMapCreator.create(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR)
    }

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

    companion object {
        private const val DEFAULT_CAPACITY = 12
        private const val DEFAULT_LOAD_FACTOR = 0.75f
    }
}

@Suppress("UNCHECKED_CAST")
abstract class ParameterCollection<P : Parameter, I : ParameterUniverseItem<P>>(
    val universe: ParameterUniverse<P, I>,
) : ParameterContainer<P>(), MutableIterable<Map.Entry<I, P>> {

    override operator fun <FUN_P : P> get(name: String): FUN_P? {
        if (isNotAllocated()) {
            return null
        }
        return getImpl(universe.getItemByNameNonNull(name)) as? FUN_P
    }

    operator fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> get(item: FUN_I): FUN_P? {
        assertItemIsPresentedInUniverse(item)
        if (isNotAllocated()) {
            return null
        }
        return getImpl(item) as? FUN_P
    }

    operator fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> set(item: FUN_I, param: FUN_P) {
        assertItemIsPresentedInUniverse(item)
        if (isNotAllocated()) {
            allocate()
        }
        putImpl(item, param)
    }

    operator fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> invoke(item: FUN_I, initBlock: FUN_P.() -> Unit) {
        assertItemIsPresentedInUniverse(item)
        if (isNotAllocated()) {
            allocate()
        }
        val param = item.createParam()
        initBlock(param)
        putImpl(item, param)
    }

    fun <FUN_P : Parameter> remove(item: ParameterUniverseItem<FUN_P>): FUN_P? {
        assertItemIsPresentedInUniverse(item)
        if (isNotAllocated()) {
            return null
        }
        return removeImpl(item) as? FUN_P?
    }

    fun remove(key: String): P? {
        if (isNotAllocated()) {
            return null
        }
        return removeImpl(universe.getItemByNameNonNull(key))
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
            putImpl(item, newParameter)
        }
    }

    abstract val size: Int

    abstract fun clear()

    protected abstract fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> getImpl(item: FUN_I): P?
    protected abstract fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> putImpl(item: FUN_I, param: FUN_P)
    protected abstract fun <FUN_P, FUN_I : ParameterUniverseItem<FUN_P>> removeImpl(item: FUN_I): P?

    abstract fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    abstract fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT

    protected abstract fun isNotAllocated(): Boolean
    protected abstract fun allocate()

    override fun copy(): ParameterCollection<P, I> {
        throw UnsupportedOperationException("Not Implemented")
    }
}