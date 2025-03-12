package kul.dataframework.core

/**
 * @author Cco0lL created 3/12/25 3:08AM
 **/

class PolymorphicUnionParameter<PO : ParameterizedObject>(
    val union: PolymorphicUnion<PO>,
    defaultType: String
): ParameterizedObjectParameter<PO>(union.create(defaultType)) {

    override var value: PO = super.value

    override fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT) {
        value = union.read(readCtx, element)
    }
}

class PolymorphicUnion<PO : ParameterizedObject>(
    vararg typeCreators: Pair<String, () -> PO>
) {

    private val typeCreators = mapOf(*typeCreators)

    fun create(type: String): PO {
        val obj = typeCreators[type]!!()
        val unionTypeParameter = StringParameter(type)
        unionTypeParameter.name = "_unionType"
        obj._injectParameter(unionTypeParameter)
        return obj
    }

    fun <ELEMENT, OBJECT> read(readCtx: ReadContext<ELEMENT, OBJECT>, element: ELEMENT): PO {
        val asObject = readCtx.elementAsObject(element)
        val currentTypeKey = readCtx.readString("_unionType", asObject)
        val obj = create(currentTypeKey)
        obj.read(readCtx, asObject)
        return obj
    }
}