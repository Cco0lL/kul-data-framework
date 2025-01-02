package kul.dataframework.core

import java.util.ArrayList
import java.util.TreeMap

/**
 * @author Cco0lL created 9/26/24 3:27AM
 **/
class ParameterList<P : Parameter>(
    universe: ParameterUniverse<P>,
) : ParameterCollection<P>(universe) {

    private var backingList = arrayListOf<P>()

    override val size get() = backingList.size

    @Suppress("UNCHECKED_CAST")
    override fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P, out ParameterMetaData>): FUN_P? {
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

    override fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P, out ParameterMetaData>) {
        if (size == 0) {
            val p1 = backingList[0]
            if (universeItem === universe.getItemNonNull(p1))
                backingList.removeAt(0)
        } else {
            val i = backingList.binarySearchBy(universeItem.ordinal) {
                universe.ordinalOf(it)
            }
            if (i >= 0)
                backingList.removeAt(i)
        }
    }

    fun remove(index: Int) {
        backingList.removeAt(index)
    }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
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

    override fun copy(): ParameterList<P> {
        return ParameterList(universe).modifyIt {
            it.read(this, needClearBeforeRead = false)
        }
    }

    override fun iterator(): MutableIterator<P> {
        return backingList.iterator()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterList<*>

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

class ParameterMap<P : Parameter>(
    universe: ParameterUniverse<P>,
) : ParameterCollection<P>(universe) {

    private val backingMap = TreeMap<ParameterUniverseItem<out P, out ParameterMetaData>, P>()

    override val size get() = backingMap.size

    @Suppress("UNCHECKED_CAST")
    override fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P, out ParameterMetaData>): FUN_P? {
        return backingMap[universeItem] as? FUN_P
    }

    override fun add(param: P) {
        val item = universe.getItemNonNull(param)
        backingMap[item] = param
    }

    override fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P, out ParameterMetaData>) {
        backingMap.remove(universeItem)
    }

    fun contains(param: P) = backingMap.containsKey(universe.getItemNonNull(param))
    fun contains(item: ParameterUniverseItem<out P, out ParameterMetaData>) = backingMap.containsKey(item)

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
            { writeCtx.stringElement(it.metaData.key, obj) },
            { _, v -> v.toElement(writeCtx, obj) }
        )
    }

    override fun clear(desiredSize: Int) { backingMap.clear() }

    override fun copy(): ParameterMap<P> {
        return ParameterMap(universe).modifyIt {
            it.read(this, needClearBeforeRead = false)
        }
    }

    override fun iterator(): Iterator<P> {
        return backingMap.values.iterator()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterMap<*>

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

abstract class ParameterCollection<P : Parameter>(
    val universe: ParameterUniverse<P>,
) : ParameterContainer<P>() {

    abstract val size: Int

    override operator fun get(key: String) = get(universe.getItemNonNull(key))
    abstract operator fun <FUN_P : P> get(universeItem: ParameterUniverseItem<FUN_P, out ParameterMetaData>): FUN_P?

    operator fun plusAssign(param: P) { add(param) }
    abstract fun add(param: P)

    operator fun <FUN_P : P> plusAssign(universeItem: ParameterUniverseItem<FUN_P, out ParameterMetaData>) { add(universeItem) }
    fun <FUN_P : P> add(universeItem: ParameterUniverseItem<FUN_P, *>, initBlock: (FUN_P.() -> Unit)? = null) {
        val param = universeItem.createParam()
        initBlock?.run { param.apply(this) }
        add(param)
    }

    operator fun minusAssign(universeItem: ParameterUniverseItem<P, out ParameterMetaData>) { remove(universeItem) }
    abstract fun <FUN_P : P> remove(universeItem: ParameterUniverseItem<FUN_P, out ParameterMetaData>)
    fun remove(key: String) { remove(universe.getItemNonNull(key)) }

    //time complexity for neither list and map is O(n*log(n))
    fun read(other: ParameterCollection<P>, needClearBeforeRead: Boolean = true) {
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

    abstract fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    abstract fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT

    abstract fun clear(desiredSize: Int)

    override fun copy(): ParameterCollection<P> {
        throw UnsupportedOperationException("Not Implemented")
    }

    protected fun toStringImpl(parameters: Collection<P>): String {
        return parameters.toString()
    }
}