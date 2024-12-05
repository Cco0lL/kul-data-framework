package kul.dataframework.core

/**
 * @author Cco0lL created 8/3/24 12:52 AM
 **/

/**
 * Represent context of writing values from serialization data/object. The interface was designed
 * to make it as lightweight as possible. Usually you need to create some reader and writer, which is
 * standalone object. The context is a singleton, and all get/read operations require to provide
 * writeable object, also it designed to support 2 concepts of serialization together
 * (byte-stream and structured "key - value"), this causes a little syntax overhead, like you need to
 * create an object, then write whatever you want into it and then convert it to an element and
 * return it
 *
 * NOTE: THIS IS IMPORTANT TO KEEP READ AND WRITE ORDER, BECAUSE "BYTE-STREAM" IMPLEMENTATIONS
 * WRITE IT AS STREAM IN ORDER OF WRITING WITHOUT KEY TO AVOID KEY-WRITING OVERHEAD
 */
interface WriteContext<ELEMENT, OBJECT> {

    //The method is NOOP for "byte-stream" protocols because all
    // writes are happen when creating an element
    fun writeElement(key: String, obj: OBJECT, element: ELEMENT)

    //Writes data to object if protocol is "byte-stream"
    fun objectAsElement(obj: OBJECT): ELEMENT

    //returns element if protocol is "byte-stream"
    fun createChildObject(obj: OBJECT): OBJECT

    /* *********************************************************************************************
                                               Write methods
      ********************************************************************************************* */

    fun writeObject(key: String, obj: OBJECT, value: OBJECT) { writeElement(key, obj, objectAsElement(value)) }
    fun writeInt(key: String, obj: OBJECT, value: Int) { writeElement(key, obj, intElement(value, obj)) }
    fun writeFloat(key: String, obj: OBJECT, value: Float) { writeElement(key, obj, floatElement(value, obj)) }
    fun writeLong(key: String, obj: OBJECT, value: Long) { writeElement(key, obj, longElement(value, obj)) }
    fun writeDouble(key: String, obj: OBJECT, value: Double) { writeElement(key, obj, doubleElement(value, obj)) }
    fun writeString(key: String, obj: OBJECT, value: String) { writeElement(key, obj, stringElement(value, obj)) }
    fun writeEnum(key: String, obj: OBJECT, value: Enum<*>) { writeElement(key, obj, enumElement(value, obj)) }

    fun <ITEM> writeCollection(
        key: String,
        collection: Collection<ITEM>,
        obj: OBJECT,
        collectionItemSupplier: (ITEM) -> ELEMENT
    ) {
        writeElement(key, obj, collectionElement(collection, obj, collectionItemSupplier))
    }

    fun <K, V> writeMap(
        key: String,
        map: Map<out K, V>,
        obj: OBJECT,
        keySupplier: (K) -> ELEMENT,
        valueSupplier: (K, V) -> ELEMENT
    ) {
        writeElement(key, obj, mapElement(map, obj, keySupplier, valueSupplier))
    }

    /* *********************************************************************************************
                                   Value to object convert methods
       ********************************************************************************************* */

    fun intElement(value: Int, obj: OBJECT): ELEMENT
    fun floatElement(value: Float, obj: OBJECT): ELEMENT
    fun longElement(value: Long, obj: OBJECT): ELEMENT
    fun doubleElement(value: Double, obj: OBJECT): ELEMENT
    fun stringElement(value: String, obj: OBJECT): ELEMENT
    fun enumElement(value: Enum<*>, obj: OBJECT): ELEMENT

    fun <ITEM> collectionElement(
        collection: Collection<ITEM>, obj: OBJECT, collectionItemSupplier: (ITEM) -> ELEMENT
    ): ELEMENT

    fun <K, V> mapElement(
        map: Map<out K, V>,
        obj: OBJECT,
        keySupplier: (K) -> ELEMENT,
        valueSupplier: (K, V) -> ELEMENT
    ): ELEMENT
}