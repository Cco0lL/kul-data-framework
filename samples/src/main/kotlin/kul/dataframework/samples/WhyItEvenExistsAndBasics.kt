package kul.dataframework.samples

import kul.dataframework.core.*
import kul.dataframework.core.reactive.ObservableFloatParameter
import kul.dataframework.core.reactive.ObservableStringParameter
import kul.dataframework.core.reactive.watch
import kul.dataframework.serialization.gson.readJson
import kul.dataframework.serialization.gson.toJson
import kotlin.reflect.KProperty

/**
 * @author Cco0lL created 11/29/24 8:19PM
 **/
class PartOfSomethingNew: ParameterizedObject() {
    var somePieceOfBeauty: String by StringParameter()
    var magicNumber: Float by ObservableFloatParameter()
    var reactiveMagicField: String by ObservableStringParameter()
}

fun main() {
    val partOfSomethingNew = PartOfSomethingNew()

    //every operation of modification should be in this block
    partOfSomethingNew.modify {
        somePieceOfBeauty = "you will never find something beauty in me, i just have an ugly soul"
    }

    partOfSomethingNew.watch {
        println(magicNumber)
    }

    //if you need more control of modification scope, you can use this case
    partOfSomethingNew.startModifyFence()
    try {
        partOfSomethingNew.magicNumber = 10f
    } finally {
        partOfSomethingNew.stopModifyFence()
    }

    partOfSomethingNew.modify {
        somePieceOfBeauty = "wealth or knowledge..... so whats you gonna take?"
    }

    //reactivity example
    partOfSomethingNew.watch {
        println(reactiveMagicField + somePieceOfBeauty + "$magicNumber")
    }


    partOfSomethingNew.modify {
        reactiveMagicField = "or not? :thinking:" // will print "or not? :thinking:"
    }

    //serialization example
    val jsonObject = partOfSomethingNew.toJson()
    println(jsonObject.toString())
    val partOfSomethingNew2 = PartOfSomethingNew().readJson(jsonObject)

    partOfSomethingNew2.read {
        println(somePieceOfBeauty)
        println(magicNumber)
    }
}
