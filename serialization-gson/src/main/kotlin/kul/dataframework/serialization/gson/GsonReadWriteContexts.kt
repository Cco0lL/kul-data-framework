package kul.dataframework.serialization.gson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kul.dataframework.core.ReadContext
import kul.dataframework.core.WriteContext

/**
 * @author Cco0lL created 8/5/24 6:53 PM
 **/
object GsonReadContext: ReadContext<JsonElement, JsonObject> {

    override fun getElement(key: String, obj: JsonObject): JsonElement { return obj[key]!! }

    override fun containsKey(obj: JsonObject, key: String): Boolean {
        return obj.has(key)
    }

    override fun elementAsString(element: JsonElement): String { return element.asString }
    override fun elementAsInt(element: JsonElement): Int { return element.asInt }
    override fun elementAsFloat(element: JsonElement): Float { return element.asFloat }
    override fun elementAsLong(element: JsonElement): Long { return element.asLong }
    override fun elementAsDouble(element: JsonElement): Double { return element.asDouble }
    override fun <T : Enum<T>> elementAsEnum(element: JsonElement, enumUniverse: List<T>): T {
        return enumUniverse[element.asInt]
    }

    override fun <T, C : MutableCollection<T>> elementAsCollection(
        element: JsonElement,
        collectionSupplier: () -> C,
        collectionItemSupplier: (JsonElement) -> T
    ): C {
        val collection = collectionSupplier()
        for (arrayItem in element.asJsonArray) {
            collection += collectionItemSupplier(arrayItem)
        }
        return collection
    }

    override fun <K, V, M : MutableMap<K, V>> elementAsMap(
        element: JsonElement,
        mapSupplier: () -> M,
        keySupplier: (JsonElement) -> K,
        valueSupplier: (K, JsonElement) -> V
    ): M {
        val map = mapSupplier()
        for (entryJsonElement in element.asJsonArray) {
            val entryJsonObject = entryJsonElement.asJsonObject

            val key = keySupplier(entryJsonObject["key"]!!)
            val value =  valueSupplier(key, entryJsonObject["value"]!!)

            map[key] = value
        }
        return map
    }
}

object GsonWriteContext : WriteContext<JsonElement, JsonObject> {

    override fun writeElement(key: String, obj: JsonObject, element: JsonElement) {
        obj.add(key, element)
    }

    override fun createChildObject(obj: JsonObject): JsonObject {
        return JsonObject()
    }

    override fun objectAsElement(obj: JsonObject): JsonElement { return obj }

    override fun intElement(value: Int, obj: JsonObject): JsonElement { return JsonPrimitive(value) }
    override fun floatElement(value: Float, obj: JsonObject): JsonElement { return JsonPrimitive(value) }
    override fun longElement(value: Long, obj: JsonObject): JsonElement { return JsonPrimitive(value) }
    override fun doubleElement(value: Double, obj: JsonObject): JsonElement { return JsonPrimitive(value) }
    override fun stringElement(value: String, obj: JsonObject): JsonElement { return JsonPrimitive(value) }
    override fun enumElement(value: Enum<*>, obj: JsonObject): JsonElement { return JsonPrimitive(value.ordinal) }

    override fun <ITEM> collectionElement(
        collection: Collection<ITEM>,
        obj: JsonObject,
        collectionItemSupplier: (ITEM) -> JsonElement
    ): JsonElement {
        return JsonArray().apply {
            for (item in collection) {
                add(collectionItemSupplier(item))
            }
        }
    }

    override fun <K, V> mapElement(
        map: Map<out K, V>,
        obj: JsonObject,
        keySupplier: (K) -> JsonElement,
        valueSupplier: (K, V) -> JsonElement
    ): JsonElement {
        return JsonArray().apply {
            for ((k, v) in map) {
                add(JsonObject().apply {
                    add("key", keySupplier(k))
                    add("value", valueSupplier(k, v))
                })
            }
        }
    }
}