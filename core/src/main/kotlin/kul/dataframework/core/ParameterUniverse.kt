package kul.dataframework.core

import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KProperty

/**
 * @author Cco0lL created 9/22/24 5:08PM
 **/
@Suppress("UNCHECKED_CAST")
open class ParameterUniverse<P : Parameter, I : ParameterUniverseItem<P>>(val key: String): Iterable<I> {

    private val itemsByName: MutableMap<String, I> = HashMap()
    private val organizedByOrdinal = arrayListOf<I>()

    val all = Collections.unmodifiableList(organizedByOrdinal)

    fun getItemByName(key: String) = itemsByName[key]
    fun getItemByOrdinal(ordinal: Int) = organizedByOrdinal[ordinal]

    fun getItemByNameNonNull(key: String): I {
        val item = itemsByName[key]
        if (item === null) {
            throw IllegalArgumentException("$key is not in $itemsByName")
        }
        return item
    }

    val size get() = itemsByName.size

    override fun iterator() = all.iterator()

    override fun toString() = "universe: $key"

    operator fun <FUN_I : ParameterUniverseItem<*>> FUN_I.provideDelegate(
        thisRef: ParameterUniverse<P, I>,
        property: KProperty<*>
    ): FUN_I {
        this.name = property.name
        _setOrdinal(organizedByOrdinal.size)
        val castedThis = this as I
        thisRef.organizedByOrdinal += castedThis
        thisRef.itemsByName[name] = castedThis
        return this
    }

    operator fun <FUN_I : ParameterUniverseItem<*>> FUN_I.getValue(
        thisRef: ParameterUniverse<P, I>,
        property: KProperty<*>
    ): FUN_I { return this }
}
