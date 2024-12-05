package kul.dataframework.core

/**
 * @author Cco0lL created 9/26/24 3:27AM
 **/
class ParameterList<P : Parameter>(
    universe: ParameterUniverse<P>,
    rootContainer: ParameterContainer<*>? = null,
) : ParameterCollection<P>(universe, rootContainer) {

    val backingList = arrayListOf<P>()

    override val size get() = backingList.size

    override fun get(name: String): P {
        return backingList.first { it.metaData.key == name }
    }

    override fun add(param: P) {
        checkIsModificationsEnabled()
        backingList += param
    }

    override fun remove(name: String) {
        checkIsModificationsEnabled()
        val itr = backingList.iterator()
        while (itr.hasNext()) {
            if (itr.next().metaData.key == name) {
                itr.remove()
                break
            }
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
                universe.create(name, rootContainer ?: this).apply {
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

    private val backingMap = linkedMapOf<String, P>()

    override val size get() = backingMap.size

    override fun get(name: String): P {
        return backingMap[name]!!
    }

    override fun add(param: P) {
        checkIsModificationsEnabled()
        backingMap[param.metaData.key] = param
    }

    override fun remove(name: String) {
        checkIsModificationsEnabled()
        backingMap.remove(name)
    }

    fun containsWithName(name: String) = backingMap.containsKey(name)
    fun contains(param: P) = containsWithName(param.metaData.key)

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        checkIsModificationsEnabled()
        readCtx.elementAsMap(
            element,
            { backingMap },
            { readCtx.elementAsString(it) },
            { k, e -> universe.create(k, rootContainer ?: this).apply { read(readCtx, e) } }
        )
    }

    override fun <ELEMENT, OBJECT> toElement(writeCtx: WriteContext<ELEMENT, OBJECT>, obj: OBJECT): ELEMENT {
        return writeCtx.mapElement(
            backingMap, obj,
            { writeCtx.stringElement(it, obj) },
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

    @Suppress("UNCHECKED_CAST")
    operator fun <FUN_P : P> get(universeItem: ParameterUniverse.Item<FUN_P, *>): FUN_P =
        get(universeItem.metaData.key) as FUN_P

    operator fun plusAssign(param: P) { add(param) }
    abstract fun add(param: P)

    operator fun <FUN_P : P> plusAssign(universeItem: ParameterUniverse.Item<FUN_P, *>) { add(universeItem) }
    fun <FUN_P : P> add(universeItem: ParameterUniverse.Item<FUN_P, *>, initBlock: (FUN_P.() -> Unit)? = null) {
        val param = universeItem.createParam(rootContainer ?: this)
        initBlock?.run { param.apply(this) }
        add(param)
    }

    operator fun minusAssign(universeItem: ParameterUniverse.Item<P, *>) { remove(universeItem) }
    fun remove(universeItem: ParameterUniverse.Item<P, *>) { remove(universeItem.metaData.key) }
    abstract fun remove(name: String)

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