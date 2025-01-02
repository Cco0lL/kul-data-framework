package kul.dataframework.core.reactive

import kul.dataframework.core.*

/**
 * @author Cco0lL created 1/2/25 2:08AM
 **/

open class ObservableBooleanParameter(
    metadata: ParameterMetaData,
    initialValue: Boolean = false
): BooleanParameter(metadata, initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Boolean = initialValue
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return field
        }
        set(value) {
            field = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableIntParameter(
    metaData: ParameterMetaData,
    initialValue: Int = 0
): IntParameter(metaData, initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Int = initialValue
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return field
        }
        set(value) {
            field = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableFloatParameter(
    metaData: ParameterMetaData,
    initialValue: Float = 0f
): FloatParameter(metaData, initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Float = initialValue
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return field
        }
        set(value) {
            field = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableLongParameter(
    metaData: ParameterMetaData,
    initialValue: Long = 0
): LongParameter(metaData, initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Long = initialValue
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return field
        }
        set(value) {
            field = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableDoubleParameter(
    metaData: ParameterMetaData,
    initialValue: Double = 0.0
): DoubleParameter(metaData, initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Double = initialValue
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return field
        }
        set(value) {
            field = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableStringParameter(
    metaData: ParameterMetaData,
    initialValue: String = "-"
): StringParameter(metaData, initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: String = initialValue
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return field
        }
        set(value) {
            field = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableEnumParameter<T : Enum<T>>(
    metaData: EnumParameterMetadata<T>,
    initialValue: T = metaData.enumUniverse[0]
): EnumParameter<T>(metaData, initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: T = initialValue
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return field
        }
        set(value) {
            field = value
            for (subscriber in subscribers)
                subscriber()
        }
}