package kul.dataframework.serialization.gson

import com.google.gson.JsonObject
import kul.dataframework.core.ParameterizedObject
import kul.dataframework.core.readIt
import kul.dataframework.core.modify

/**
 * @author Cco0lL created 11/29/24 8:39PM
 **/
fun ParameterizedObject.toJson(): JsonObject {
    return readIt {
        JsonObject().also { write(GsonWriteContext, it) }
    }
}

fun <O : ParameterizedObject> O.readJson(jsonObject: JsonObject): O {
    return modify {
        read(GsonReadContext, jsonObject)
    }
}