package kul.dataframework.core

import kotlin.reflect.KProperty

/**
 * @author Cco0lL created 9/22/24 5:08PM
 **/
open class ParameterUniverse<out P : Parameter, I : ParameterUniverseItem<P>>(val key: String): Iterable<I> {

    private val universe: MutableMap<String, I> = LinkedHashMap(2, 1f)

    operator fun <FUN_I : I> FUN_I.provideDelegate(
        thisRef: ParameterUniverse<@UnsafeVariance P, I>,
        property: KProperty<*>
    ): FUN_I {
        this.name = property.name
        _setOrdinal(thisRef.size)
        thisRef.universe[name] = this
        return this
    }

    operator fun <FUN_I : I> FUN_I.getValue(
        thisRef: ParameterUniverse<*, *>,
        property: KProperty<*>
    ): FUN_I { return this }

    fun getItem(parameter: @UnsafeVariance P) = getItem(parameter.name)
    fun getItem(key: String) = universe[key]

    fun create(key: String): P = getItemNonNull(key).createParam()

    fun getItemNonNull(param: @UnsafeVariance P) = getItemNonNull(param.name)
    fun getItemNonNull(key: String): I {
        val item = universe[key]
        if (item === null) {
            throw IllegalArgumentException("$key is not in $universe")
        }
        return item
    }

    val size get() = universe.size

    override fun iterator() = universe.values.iterator()

    override fun toString() = "universe: $key"
}
