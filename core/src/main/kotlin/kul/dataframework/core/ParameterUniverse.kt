package kul.dataframework.core

import kotlin.check

/**
 * @author Cco0lL created 9/22/24 5:08PM
 **/
class ParameterUniverse<P : Parameter> private constructor(
    val type: String,
    private val universe: Map<String, Item<out P, out ParameterMetaData>>
): Iterable<ParameterUniverse.Item<out P, *>> {

    init {
        for ((index, item) in universe.values.withIndex()) {
            (item as ItemImpl<*,*>)._setOrdinal(index)
        }
    }

    fun getItem(key: String) = universe[key]
    fun getItemForParam(parameter: P) = getItem(parameter.metaData.key)

    fun create(key: String, parameterCollection: ParameterCollection<*>) =
        getItemNonNull(key).createParam(parameterCollection)

    fun ordinalOf(param: P): Int { return getItemNonNull(param).ordinal }

    @Suppress("UNCHECKED_CAST")
    fun <FUN_P : P> getItemNonNull(param: FUN_P) =
        getItemNonNull(param.metaData.key) as Item<FUN_P, out ParameterMetaData>

    fun getItemNonNull(key: String): Item<out P, out ParameterMetaData> {
        val item = universe[key]
        if (item === null) {
            throw IllegalArgumentException("$key is not in $universe")
        }
        return item
    }

    val size get() = universe.size
    override fun iterator() = universe.values.iterator()

    override fun toString() = "universe: $type"

    sealed class Item<P : Parameter, M : ParameterMetaData>(
        val metaData: M,
        val creator: (ParameterCollection<*>, M) -> P
    ): Comparable<Item<P, out ParameterMetaData>> {

        var ordinal = -1
            protected set

        fun createParam(parameterCollection: ParameterCollection<*>) = creator(parameterCollection, metaData)

        override fun compareTo(other: Item<P, out ParameterMetaData>): Int {
            val ordinal = ordinal
            val otherOrdinal = other.ordinal
            return when {
                ordinal > otherOrdinal -> 1
                ordinal < otherOrdinal -> -1
                else -> 0
            }
        }
    }

    private class ItemImpl<P : Parameter, M : ParameterMetaData>(
        metaData: M,
        creator: (ParameterCollection<*>, M) -> P
    ) : Item<P, M>(metaData, creator) {

        fun _setOrdinal(value: Int) {
            check(ordinal == -1) { "can't set ordinal since it already in universe" }
            ordinal = value
        }
    }

    companion object {
        fun <P : Parameter> of(
            type: String,
            vararg items: Item<out P, out ParameterMetaData>
        ) = ParameterUniverse(type, items.associateBy { it.metaData.key })

        fun <P : Parameter, M : ParameterMetaData> newItem(
            metadata: M,
            creator: (ParameterContainer<*>, M) -> P
        ): Item<P, M> = ItemImpl(metadata, creator)
    }
}
