package kul.dataframework.samples

import kul.dataframework.core.FloatParameter
import kul.dataframework.core.IntParameter
import kul.dataframework.core.ParameterizedObject
import kul.dataframework.core.PolymorphicUnion
import kul.dataframework.serialization.gson.GsonReadContext
import kul.dataframework.serialization.gson.toJson

/**
 * @author Cco0lL created 3/12/25 3:12PM
 **/
open class First: ParameterizedObject() {
    var firstInt by IntParameter()
    var firstFloat by FloatParameter()
}

open class Second: First() {
    var secondInt by IntParameter()
    var secondFloat by FloatParameter()
}

open class Third: First() {
    var thirdInt by IntParameter()
    var thirdFloat by FloatParameter()
}

val Union = PolymorphicUnion(
    "second" to { Second() },
    "third" to { Third() }
)

fun main() {
    val second = Union.create("second")
    val third = Union.create("third")

    val secondAsJson = second.toJson()
    val thirdAsJson = third.toJson()

    println(secondAsJson)
    println(thirdAsJson)

    val asSecondFromJson = Union.read(GsonReadContext, secondAsJson)
    val asThirdFromJson = Union.read(GsonReadContext, thirdAsJson)

    print(asSecondFromJson)
    println(asThirdFromJson)
}
