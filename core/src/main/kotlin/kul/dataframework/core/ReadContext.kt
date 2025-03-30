package kul.dataframework.core

import java.util.function.IntFunction

/**
 * @author Cco0lL created 7/21/24 2:32 PM
 **/

/**
 * Represent context of reading values from serialization data/object. The interface was designed
 * to make it as lightweight as possible. Usually you need to create some reader and writer, which is
 * standalone object. The context is a singleton, and all get/read operations require to provide
 * readable object, also it designed to support 2 concepts of serialization together
 * (byte-stream and structured "key - value"), this causes a little syntax overhead
 *
 * NOTE: THIS IS IMPORTANT TO KEEP READ AND WRITE ORDER, BECAUSE "BYTE-STREAM" IMPLEMENTATIONS
 * READ IT AS STREAM TO AVOID KEY-WRITING OVERHEAD
 */
interface ReadContext<ELEMENT, OBJECT> {

    /**
     * @return - obj if protocol is "byte-stream", new element otherwise
     */
    fun getElement(key: String, obj: OBJECT): ELEMENT
    fun getObject(key: String, obj: OBJECT) = elementAsObject(getElement(key, obj))

    fun containsKey(obj: OBJECT, key: String): Boolean

    /* *********************************************************************************************
                                        direct primitives read
      ********************************************************************************************* */

    fun readString(key: String, obj: OBJECT) = elementAsString(getElement(key, obj))
    fun readInt(key: String, obj: OBJECT) = elementAsInt(getElement(key, obj))
    fun readFloat(key: String, obj: OBJECT) = elementAsFloat(getElement(key, obj))
    fun readLong(key: String, obj: OBJECT) = elementAsLong(getElement(key, obj))
    fun readDouble(key: String, readable: OBJECT) = elementAsDouble(getElement(key, readable))
    fun <T : Enum<T>> readEnum(key: String, obj: OBJECT, enumUniverse: List<T>) = elementAsEnum(getElement(key, obj), enumUniverse)

    fun <T, C : MutableCollection<T>> readCollection (
        key: String,
        obj: OBJECT,
        collectionSupplier: IntFunction<C>,
        collectionItemSupplier: (ELEMENT) -> T
    ) = elementAsCollection(
        getElement(key, obj),
        collectionSupplier,
        collectionItemSupplier
    )

    fun <K, V, M : MutableMap<K, V>> readMap(
        key: String,
        obj: OBJECT,
        mapSupplier: IntFunction<M>,
        keySupplier: (ELEMENT) -> K,
        valueSupplier: (K, ELEMENT) -> V
    ) = elementAsMap(getElement(key, obj), mapSupplier, keySupplier, valueSupplier)

    /* *********************************************************************************************
                                      Element convertors to explicit values
      ********************************************************************************************* */

    @Suppress("UNCHECKED_CAST")
    fun elementAsObject(element: ELEMENT): OBJECT { return element as OBJECT }

    fun elementAsString(element: ELEMENT): String
    fun elementAsInt(element: ELEMENT): Int
    fun elementAsFloat(element: ELEMENT): Float
    fun elementAsLong(element: ELEMENT): Long
    fun elementAsDouble(element: ELEMENT): Double
    fun <T : Enum<T>> elementAsEnum(element: ELEMENT, enumUniverse: List<T>): T

    fun <T, C : MutableCollection<T>> elementAsCollection(
        element: ELEMENT,
        collectionSupplier: IntFunction<C>,
        collectionItemSupplier: (ELEMENT) -> T
    ): C

    fun <K, V, M : MutableMap<K, V>> elementAsMap(
        element: ELEMENT,
        mapSupplier: IntFunction<M>,
        keySupplier: (ELEMENT) -> K,
        valueSupplier : (K, ELEMENT) -> V
    ): M
}
