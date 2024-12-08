package kul.dataframework.core

import kotlin.check

/**
 * @author Cco0lL created 9/22/24 5:08PM
 **/
class ParameterUniverse<P : Parameter> private constructor(
    val type: String,
    private val universe: Map<String, Item<out P, *>>
): Iterable<ParameterUniverse.Item<out P, *>> {

    init {
        for ((index, item) in universe.values.withIndex()) {
            (item as ItemImpl<*,*>)._setOrdinal(index)
        }
    }

    fun getItem(key: String) = universe[key]
    fun getItemForParam(parameter: P) = getItem(parameter.metaData.key)

    fun create(key: String, containerOwner: ParameterContainer<*>) = universe[key]!!.createParam(containerOwner)

    fun ordinalOf(param: P): Int { return getItemForParam(param)!!.ordinal }

    val size get() = universe.size
    override fun iterator() = universe.values.iterator()

    override fun toString() = "universe: [type: ${type}]"

    sealed class Item<P : Parameter, M : ParameterMetaData>(
        val metaData: M,
        val creator: (ParameterContainer<*>, M) -> P
    ) {

        var ordinal = -1
            protected set

        fun createParam(containerOwner: ParameterContainer<*>) = creator(containerOwner, metaData)
    }

    private class ItemImpl<P : Parameter, M : ParameterMetaData>(
        metaData: M,
        creator: (ParameterContainer<*>, M) -> P
    ) : Item<P, M>(metaData, creator) {

        fun _setOrdinal(value: Int) {
            check(ordinal != -1) { "can't set ordinal since it already has been set" }
            ordinal = value
        }
    }

    companion object {
        fun <P : Parameter> of(
            type: String,
            vararg items: Item<out P, *>
        ) = ParameterUniverse(type, items.associateBy { it.metaData.key })

        fun <P : Parameter, M : ParameterMetaData> newItem(
            metadata: M,
            creator: (ParameterContainer<*>, M) -> P
        ): Item<P, M> = ItemImpl(metadata, creator)
    }
}
