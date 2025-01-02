package kul.dataframework.core

/**
 * @author Cco0lL created 9/22/24 5:08PM
 **/
class ParameterUniverse<P : Parameter> private constructor(
    val type: String,
    private val universe: Map<String, ParameterUniverseItem<out P, out ParameterMetaData>>
): Iterable<ParameterUniverseItem<out P, *>> {

    init {
        for ((index, item) in universe.values.withIndex()) {
            item._setOrdinal(index)
        }
    }

    fun getItem(key: String) = universe[key]
    fun getItemForParam(parameter: P) = getItem(parameter.metaData.key)

    fun create(key: String) =
        getItemNonNull(key).createParam()

    fun ordinalOf(param: P): Int { return getItemNonNull(param).ordinal }

    @Suppress("UNCHECKED_CAST")
    fun <FUN_P : P> getItemNonNull(param: FUN_P) =
        getItemNonNull(param.metaData.key) as ParameterUniverseItem<FUN_P, out ParameterMetaData>

    fun getItemNonNull(key: String): ParameterUniverseItem<out P, out ParameterMetaData> {
        val item = universe[key]
        if (item === null) {
            throw IllegalArgumentException("$key is not in $universe")
        }
        return item
    }

    val size get() = universe.size
    override fun iterator() = universe.values.iterator()

    override fun toString() = "universe: $type"

    companion object {
        fun <P : Parameter> of(
            type: String,
            vararg items: ParameterUniverseItem<out P, out ParameterMetaData>
        ) = ParameterUniverse(type, items.associateBy { it.metaData.key })
    }
}
