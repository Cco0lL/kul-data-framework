package kul.dataframework.samples

import com.google.gson.JsonObject
import kul.dataframework.core.*
import kul.dataframework.serialization.gson.GsonWriteContext

/**
 * @author Cco0lL created 3/12/25 4:07PM
 **/
class TestUniverseItem(
    creator: () -> IntParameter
) : ParameterUniverseItem<IntParameter>(creator) {
}

object TestUniverse: ParameterUniverse<Parameter, ParameterUniverseItem<Parameter>>("test") {
    val Item1 by TestUniverseItem { IntParameter() }
    val Item2 by TestUniverseItem { IntParameter() }
}

fun main() {
    val parameterDictionary = ParameterDictionary(TestUniverse)
    parameterDictionary.add(TestUniverse.Item1) { value = 1 }
    parameterDictionary.add(TestUniverse.Item2) { value = 2 }
    println(parameterDictionary)
    println(parameterDictionary.toElement(GsonWriteContext, JsonObject()))
}