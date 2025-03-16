package kul.dataframework.core

import kotlin.reflect.KProperty

/**
 * @author Cco0lL created 9/22/24 5:08PM
 **/
@Suppress("UNCHECKED_CAST")
open class ParameterUniverse<P : Parameter, I : ParameterUniverseItem<P>>(val key: String): Iterable<I> {

    private val universe: MutableMap<String, I> = LinkedHashMap(2, 1f)

    operator fun <FUN_I : ParameterUniverseItem<*>> FUN_I.provideDelegate(
        thisRef: ParameterUniverse<P, I>,
        property: KProperty<*>
    ): FUN_I {
        this.name = property.name
        _setOrdinal(thisRef.size)
        thisRef.universe[name] = this as I
        return this
    }

    operator fun <FUN_I : ParameterUniverseItem<*>> FUN_I.getValue(
        thisRef: ParameterUniverse<P, I>,
        property: KProperty<*>
    ): FUN_I { return this }

    fun getItem(parameter: P) = getItem(parameter.name)
    fun getItem(key: String) = universe[key]

    fun create(key: String): P = getItemNonNull(key).createParam()

    fun getItemNonNull(param: P) = getItemNonNull(param.name)
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
