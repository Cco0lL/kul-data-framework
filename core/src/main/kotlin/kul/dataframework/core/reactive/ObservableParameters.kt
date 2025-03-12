package kul.dataframework.core.reactive

import kul.dataframework.core.*
import kotlin.enums.EnumEntries

/**
 * @author Cco0lL created 1/2/25 2:08AM
 **/

open class ObservableBooleanParameter(initialValue: Boolean = false): BooleanParameter(initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Boolean
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return super.value
        }
        set(value) {
            super.value = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableIntParameter(initialValue: Int = 0): IntParameter(initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Int
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return super.value
        }
        set(value) {
            super.value = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableFloatParameter(initialValue: Float = 0f): FloatParameter(initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Float
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return super.value
        }
        set(value) {
            super.value = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableLongParameter(initialValue: Long = 0): LongParameter(initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Long
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return super.value
        }
        set(value) {
            super.value = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableDoubleParameter(initialValue: Double = 0.0): DoubleParameter(initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: Double
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return super.value
        }
        set(value) {
            super.value = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableStringParameter(initialValue: String = "-"): StringParameter(initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: String
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return super.value
        }
        set(value) {
            super.value = value
            for (subscriber in subscribers)
                subscriber()
        }
}

open class ObservableEnumParameter<T : Enum<T>>(
    enumEntries: EnumEntries<T>,
    initialValue: T = enumEntries[0]
): EnumParameter<T>(enumEntries, initialValue) {

    private val subscribers = ArrayList<ParameterSubscriber>(2)

    override var value: T
        get() {
            val atomicSubscriber = atomicSubscriber
            if (atomicSubscriber !== null)
                subscribers.add(atomicSubscriber)
            return super.value
        }
        set(value) {
            super.value = value
            for (subscriber in subscribers)
                subscriber()
        }
}