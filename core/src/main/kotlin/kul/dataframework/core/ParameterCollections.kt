package kul.dataframework.core

import java.util.TreeMap

/**
 * @author Cco0lL created 9/26/24 3:27AM
 **/
class ParameterList<P : Parameter>(
    universe: ParameterUniverse<P>,
    rootContainer: ParameterContainer<*>? = null,
) : ParameterCollection<P>(universe, rootContainer) {

    val backingList = arrayListOf<P>()

    override val size get() = backingList.size


    @Suppress("UNCHECKED_CAST")
    override fun <FUN_P : P> get(universeItem: ParameterUniverse.Item<FUN_P, out ParameterMetaData>): FUN_P? {
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
        checkIsModificationsEnabled()
        addAndReturnIndex(param)
    }

    fun addAndReturnIndex(param: P): Int {
        checkIsModificationsEnabled()
        var i: Int
        val s = size
        if (s == 0) {
            backingList += param
            i = 0
        } else {
            val pi = universe.getItemNonNull(param)
            if (s == 1) {
                val p1 = backingList[0]
                val p1o = universe.getItemNonNull(p1)
                if (pi >= p1o) {
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
        }
        return i
    }

    override fun <FUN_P : P> remove(universeItem: ParameterUniverse.Item<FUN_P, out ParameterMetaData>) {
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
        checkIsModificationsEnabled()
        backingList.removeAt(index)
    }

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        checkIsModificationsEnabled()
        readCtx.elementAsCollection(
            element,
            { backingList },
            {
                val itemObject = readCtx.elementAsObject(it)
                val name = readCtx.readString("key", itemObject)
                universe.create(name, this).apply {
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

    override fun copy(rootContainer: ParameterContainer<*>?): ParameterList<P> {
        return ParameterList(universe, rootContainer).modifyIt {
            for (parameter in this)
                @Suppress("UNCHECKED_CAST")
                it += parameter.copy() as P
        }
    }

    override fun unwrappedParametersIterator(): MutableIterator<P> {
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
    rootContainer: ParameterContainer<*>? = null
) : ParameterCollection<P>(universe, rootContainer) {

    private val backingMap = TreeMap<ParameterUniverse.Item<out P, out ParameterMetaData>, P>()

    override val size get() = backingMap.size

    @Suppress("UNCHECKED_CAST")
    override fun <FUN_P : P> get(universeItem: ParameterUniverse.Item<FUN_P, out ParameterMetaData>): FUN_P? {
        return backingMap[universeItem] as? FUN_P
    }

    override fun add(param: P) {
        checkIsModificationsEnabled()
        backingMap[universe.getItemNonNull(param)] = param
    }

    override fun <FUN_P : P> remove(universeItem: ParameterUniverse.Item<FUN_P, out ParameterMetaData>) {
        checkIsModificationsEnabled()
        backingMap.remove(universeItem)
    }

    fun containsWithKey(name: String) = backingMap.containsKey(universe.getItemNonNull(name))
    fun contains(param: P) = backingMap.containsKey(universe.getItemNonNull(param))

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        checkIsModificationsEnabled()
        readCtx.elementAsMap(
            element,
            { backingMap },
            { universe.getItemNonNull(readCtx.elementAsString(it)) },
            { k, e -> k.createParam(this).apply { read(readCtx, e) } }
        )
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.mapElement(
            backingMap, obj,
            { writeCtx.stringElement(it.metaData.key, obj) },
            { _, v -> v.toElement(writeCtx, obj) }
        )
    }

    override fun copy(rootContainer: ParameterContainer<*>?): ParameterMap<P> {
        return ParameterMap(universe, rootContainer).modifyIt {
            for (parameter in this)
                @Suppress("UNCHECKED_CAST")
                it += parameter.copy() as P
        }
    }

    override fun unwrappedParametersIterator(): MutableIterator<P> {
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
    rootContainer: ParameterContainer<*>?,
) : ParameterContainer<P>(rootContainer) {

    abstract val size: Int

    override operator fun get(key: String) = get(universe.getItemNonNull(key))
    abstract operator fun <FUN_P : P> get(universeItem: ParameterUniverse.Item<FUN_P, out ParameterMetaData>): FUN_P?

    operator fun plusAssign(param: P) { add(param) }
    abstract fun add(param: P)

    operator fun <FUN_P : P> plusAssign(universeItem: ParameterUniverse.Item<FUN_P, out ParameterMetaData>) { add(universeItem) }
    fun <FUN_P : P> add(universeItem: ParameterUniverse.Item<FUN_P, *>, initBlock: (FUN_P.() -> Unit)? = null) {
        val param = universeItem.createParam(rootContainer ?: this)
        initBlock?.run { param.apply(this) }
        add(param)
    }

    operator fun minusAssign(universeItem: ParameterUniverse.Item<P, out ParameterMetaData>) { remove(universeItem) }
    abstract fun <FUN_P : P> remove(universeItem: ParameterUniverse.Item<FUN_P, out ParameterMetaData>)
    fun remove(key: String) { remove(universe.getItemNonNull(key)) }

    abstract fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT)
    abstract fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT

    override fun copy(rootContainer: ParameterContainer<*>?): ParameterCollection<P> {
        throw IllegalArgumentException("Not Implemented")
    }

    protected abstract fun unwrappedParametersIterator(): MutableIterator<P>
    override fun iterator() = object : MutableIterator<P> {
        val iterator = unwrappedParametersIterator()
        override fun hasNext() = iterator.hasNext()
        override fun next() = iterator.next()
        override fun remove() {
            checkIsModificationsEnabled()
            iterator.remove()
        }
    }

    protected fun toStringImpl(parameters: Collection<P>): String {
        return parameters.toString()
    }
}