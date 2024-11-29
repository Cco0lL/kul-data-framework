package kul.dataframework.core

/**
 * @author Cco0lL created 9/22/24 5:08PM
 **/
class ParameterUniverse<P : Parameter> private constructor(
    val type: String,
    private val universe: Map<String, Item<out P, *>>
): Iterable<ParameterUniverse.Item<out P, *>> {

    fun create(name: String, containerOwner: ParameterContainer<*>) = universe[name]!!.createParam(containerOwner)

    val size get() = universe.size
    override fun iterator() = universe.values.iterator()

    override fun toString() = "universe: [type: ${type}]"

    class Item<P : Parameter, M : ParameterMetaData> internal constructor(
        val metaData: M,
        val creator: (ParameterContainer<*>, M) -> P
    ) {
        fun createParam(containerOwner: ParameterContainer<*>) = creator(containerOwner, metaData)
    }

    companion object {
        fun <P : Parameter> of(
            type: String,
            vararg items: Item<out P, *>
        ) = ParameterUniverse(type, items.associateBy { it.metaData.key })

        fun <P : Parameter, M : ParameterMetaData> newItem(
            metadata: M,
            creator: (ParameterContainer<*>, M) -> P
        ) = Item(metadata, creator)
    }
}
