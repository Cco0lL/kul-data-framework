package kul.dataframework.samples

import kul.dataframework.core.*
import kul.dataframework.serialization.gson.readJson
import kul.dataframework.serialization.gson.toJson

/**
 * @author Cco0lL created 11/29/24 8:19PM
 **/

class OrderInfo: ParameterizedObject() {

    var name by param(NameMeta, "some cool order")
    val position by intParam(PositionMeta, 10)

    companion object {
        val NameMeta = StringParameterMetadata("name")
        val PositionMeta = ParameterMetaData("position")
    }
}

fun main() {
    val orderInfo = OrderInfo()

    //when you use subscribe function, it subscribes to listen change of any property
    // that has been used in this block (property must belong the parameterized object called subscribe,
    // and subscribe block will run first time at function invocation, and then every time when any of
    // used properties are changed
    orderInfo.subscribe {
        println("name: ${orderInfo.name}, position: ${orderInfo.position}") //will print "name: some cool order, position: 10" immediately
    }

    //every operation of modification should be in this block
    orderInfo.modify {
        name = "not some cool order anymore" //will print "name: not some cool order anymore, position: 10"
    }

    //if you need more control of modification scope, you can use this case
    orderInfo.enableModifications()
    // makes whatever you want
    orderInfo.disableModifications()

    //serialization example
    val jsonObject = orderInfo.toJson()
    println(jsonObject.toString())
    val orderInfo2 = OrderInfo().readJson(jsonObject)

    orderInfo2.acquireRead {
        println(name)
        println(position)
    }
}
