package kul.dataframework.core

import java.util.ArrayList
import java.util.TreeMap

/**
 * @author Cco0lL created 9/26/24 3:27AM
 **/
@Suppress("UNCHECKED_CAST")
class ParameterDictionary<P : Parameter, I : ParameterUniverseItem<out P, *>>(
    universe: ParameterUniverse<P, I>,
) : ParameterCollection<P, I>(universe) {

    private var backingList = arrayListOf<P>()

    override val size get() = backingList.size

    override fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P, *>): FUN_P? {
        if (size == 0) {
            val p1 = backingList[0]
            if (universeItem === universe.getItemNonNull(p1))
                return p1 as FUN_P
        } else {
            val i = backingList.binarySearchBy(universeItem.ordinal) {
                universe.ordinalOf(it)
            }
            if (i >= 0)
                return backingList[i] as FUN_P
        }
        return null
    }

    fun get(index: Int): P {
        return backingList[index]
    }

    override fun add(param: P) {
        addAndReturnIndex(param)
    }

    fun addAndReturnIndex(param: P): Int {
        checkEnabledModifications()
        val pi = universe.getItemNonNull(param)
        var i: Int
        val s = size
        if (s == 0) {
            backingList += param
            i = 0
        } else if (s == 1) {
            val p1 = backingList[0]
            val p1i = universe.getItemNonNull(p1)
            if (pi >= p1i) {
                backingList += param
                i = 1
            } else {
                backingList.add(0, param)
                i = 0
            }
        } else {
            i = backingList.binarySearchBy(pi.ordinal) {
                universe.ordinalOf(it)
            }
            if (i < 0)
                i = -(i + 1)
            backingList.add(i, param)
        }
        return i
    }

    override fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P, *>): FUN_P? {
        checkEnabledModifications()
        if (size == 0) {
            val p1 = backingList[0]
            if (universeItem === universe.getItemNonNull(p1))
               return backingList.removeAt(0) as FUN_P
        } else {
            val i = backingList.binarySearchBy(universeItem.ordinal) {
                universe.ordinalOf(it)
            }
            if (i >= 0)
               return backingList.removeAt(i) as FUN_P
        }
        return null
    }

    fun remove(index: Int) {
        checkEnabledModifications()
        backingList.removeAt(index)
    }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        checkEnabledModifications()
        readCtx.elementAsCollection(
            element,
            { backingList },
            {
                val itemObject = readCtx.elementAsObject(it)
                val name = readCtx.readString("key", itemObject)
                universe.create(name).apply {
                    read(readCtx, readCtx.getElement("value", itemObject))
                }
            }
        )
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.collectionElement(backingList, obj) {
            val itemObject = writeCtx.createChildObject(obj)
            writeCtx.writeString("key", itemObject, it.metaData.key)
            writeCtx.writeElement("value", itemObject, it.toElement(writeCtx, itemObject))
            writeCtx.objectAsElement(itemObject)
        }
    }

    override fun clear(desiredSize: Int) {
        backingList = ArrayList(desiredSize)
    }

    override fun copy(): ParameterDictionary<P, I> {
        return ParameterDictionary(universe).modifyIt {
            it.read(this, needClearBeforeRead = false)
        }
    }

    override fun forEachWithItems(action: (I, P) -> Unit) {
        for (param in backingList) {
            action(universe.getItemNonNull(param), param)
        }
    }

    override fun backingIterator(): MutableIterator<P> {
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
        return toStringImpl(backingList)
    }
}

@Suppress("UNCHECKED_CAST")
class ParameterMap<P : Parameter, I : ParameterUniverseItem<out P, *>>(
    universe: ParameterUniverse<P, I>,
) : ParameterCollection<P, I>(universe) {

    private val backingMap = TreeMap<I, P>()

    override val size get() = backingMap.size

    override fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P, *>): FUN_P? {
        return backingMap[universeItem as I] as? FUN_P
    }

    override fun add(param: P) {
        checkEnabledModifications()
        val item = universe.getItemNonNull(param)
        backingMap[item] = param
    }

    override fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P, *>): FUN_P? {
        checkEnabledModifications()
        return backingMap.remove(universeItem as I) as? FUN_P
    }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        checkEnabledModifications()
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
            { writeCtx.stringElement(it.metaData.key, obj) },
            { _, v -> v.toElement(writeCtx, obj) }
        )
    }

    override fun clear(desiredSize: Int) { backingMap.clear() }

    override fun copy(): ParameterMap<P, I> {
        return ParameterMap(universe).modifyIt {
            it.read(this, needClearBeforeRead = false)
        }
    }

    override fun forEachWithItems(action: (I, P) -> Unit) {
        for (entry in backingMap.entries) {
            action(entry.key, entry.value)
        }
    }

    override fun backingIterator(): MutableIterator<P> {
        return backingMap.values.iterator()
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
        return toStringImpl(backingMap.values)
    }
}

@Suppress("UNCHECKED_CAST")
abstract class ParameterCollection<P : Parameter, I : ParameterUniverseItem<out P, *>>(
    val universe: ParameterUniverse<P, I>,
) : ParameterContainer<P>() {

    private var canModify = true
    abstract val size: Int

    override operator fun <FUN_P : P> get(key: String) = get(universe.getItemNonNull(key)) as FUN_P
    abstract operator fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P, *>): FUN_P?

    operator fun plusAssign(param: P) { add(param) }
    abstract fun add(param: P)

    operator fun plusAssign(universeItem: ParameterUniverseItem<out P, *>) { add(universeItem) }
    fun <FUN_P : P> add(universeItem: ParameterUniverseItem<FUN_P, *>, initBlock: (FUN_P.() -> Unit)? = null) {
        val param = universeItem.createParam()
        initBlock?.run { param.apply(this) }
        add(param)
    }

    operator fun minusAssign(universeItem: ParameterUniverseItem<out P, *>) { remove(universeItem) }
    abstract fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P, *>): FUN_P?
    fun remove(key: String): P? { return remove(universe.getItemNonNull(key)) }

    open fun contains(key: String) = contains(universe.getItemNonNull(key))
    open fun contains(universeItem: ParameterUniverseItem<out P, *>) =
        get(universeItem) != null

    override fun enableModifications() {
        canModify = true
        super.enableModifications()
    }

    override fun disableModifications() {
        canModify = false
        super.disableModifications()
    }

    //time complexity for both list and map is O(n*log(n))
    fun read(other: ParameterCollection<P, I>, needClearBeforeRead: Boolean = true) {
        checkEnabledModifications()
        val universe = universe
        assert(universe == other.universe) {
            "can't read from collection, universe of which is different"
        }
        if (needClearBeforeRead) {
            clear(desiredSize = other.size)
        }
        for (parameter in other) {
            val newParameter = universe.getItemNonNull(parameter).createParam()
            newParameter.readValueFromAnotherParameter(parameter)
            add(newParameter)
        }
    }

    abstract fun forEachWithItems(action: (I, P) -> Unit)

    protected abstract fun backingIterator(): MutableIterator<P>
    override fun iterator() = object : MutableIterator<P> {
        val backingIterator = backingIterator()
        override fun hasNext() = backingIterator.hasNext()
        override fun next() = backingIterator.next()
        override fun remove() {
            checkEnabledModifications()
            backingIterator.remove()
        }
    }

    protected fun checkEnabledModifications() {
        if (!canModify) {
            throw UnsupportedOperationException("modifications are disabled. set canModify = true to make sure" +
                    " it wasn't mistake")
        }
    }

    abstract fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    abstract fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT

    abstract fun clear(desiredSize: Int)

    override fun copy(): ParameterCollection<P, I> {
        throw UnsupportedOperationException("Not Implemented")
    }

    protected fun toStringImpl(parameters: Collection<P>): String {
        return parameters.toString()
    }
}