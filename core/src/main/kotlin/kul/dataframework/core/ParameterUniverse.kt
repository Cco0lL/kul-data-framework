package kul.dataframework.core

/**
 * @author Cco0lL created 9/22/24 5:08PM
 **/
class ParameterUniverse<P : Parameter, I : ParameterUniverseItem<out P, *>> private constructor(
    val type: String,
    private val universe: Map<String, I>
): Iterable<I> {

    init {
        for ((index, item) in universe.values.withIndex()) {
            item._setOrdinal(index)
        }
    }

    fun <FUN_P : P> getItem(parameter: P) = getItem(parameter.metaData.key)
    fun getItem(key: String) = universe[key]

    fun create(key: String): P = getItemNonNull(key).createParam()

    fun ordinalOf(param: P): Int {
        return getItemNonNull(param).ordinal
    }

    fun getItemNonNull(param: P) = getItemNonNull(param.metaData.key)
    fun getItemNonNull(key: String): I {
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
        fun <P : Parameter, I : ParameterUniverseItem<out P, *>> of(
            type: String,
            vararg items: I
        ) = ParameterUniverse(type, items.associateBy { it.metaData.key })
    }
}
