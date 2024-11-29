package kul.dataframework.samples

import kul.dataframework.core.*
import kul.dataframework.serialization.gson.readJson
import kul.dataframework.serialization.gson.toJson

/**
 * @author Cco0lL created 11/29/24 8:19PM
 **/

class PartOfSomethingNew: ParameterizedObject() {

    var somePieceOfBeauty by param(BeautyMeta, "even after the darkest night comes the dawn")
    var magicNumber by longParam(MagicNumMeta, 0xC001C0DE)

    companion object {
        val BeautyMeta = StringParameterMetadata("somePieceOfBeauty")
        val MagicNumMeta = ParameterMetaData("position")
    }
}

fun main() {
    val partOfSomethingNew = PartOfSomethingNew()
    //when you use subscribe function, it subscribes to listen change of any property
    // that has been used in this block (property must belong the parameterized object called subscribe,
    // and subscribe block will run first time at function invocation, and then every time when any of
    // used properties are changed
    partOfSomethingNew.subscribe {
        println("beauty: ${partOfSomethingNew.somePieceOfBeauty}, magicNum: ${partOfSomethingNew.magicNumber}") //will print "beauty: even after the darkest night comes the dawn,
                                                                                                                // magicNum: 3221340382" immediately
    }

    //every operation of modification should be in this block
    partOfSomethingNew.modify {
        somePieceOfBeauty = "you will never find something beauty in me, i just have an ugly soul" //will print "beauty: you will never find something beauty in me,
                                                                                                // i just have an ugly soul, magicNum: 3221340382"
    }

    //if you need more control of modification scope, you can use this case
    partOfSomethingNew.enableModifications()
    partOfSomethingNew.magicNumber = 0xC01DBABE //will print "beauty: you will never find something beauty in me,
    partOfSomethingNew.disableModifications()   // i just have an ugly soul, magicNum: 3223173822"

    partOfSomethingNew.modify {
        somePieceOfBeauty = "wealth or knowledge..... so whats you gonna take?"
    }

    //serialization example
    val jsonObject = partOfSomethingNew.toJson()
    println(jsonObject.toString())
    val partOfSomethingNew2 = PartOfSomethingNew().readJson(jsonObject)

    partOfSomethingNew2.acquireRead {
        println(somePieceOfBeauty)
        println(magicNumber)
    }
}
