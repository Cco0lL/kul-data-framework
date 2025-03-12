package kul.dataframework.core

open class ParameterUniverseItem<out P : Parameter>(val creator: () -> P): Comparable<ParameterUniverseItem<*>> {

    lateinit var name: String
        internal set
    var ordinal = -1
        private set

    internal fun _setOrdinal(value: Int) {
        check(ordinal == -1) { "can't set ordinal since it already in universe" }
        ordinal = value
    }

    fun createParam() = creator().apply { name = this@ParameterUniverseItem.name }

    override fun compareTo(other: ParameterUniverseItem<*>): Int {
        val ordinal = ordinal
        val otherOrdinal = other.ordinal
        return when {
            ordinal > otherOrdinal -> 1
            ordinal < otherOrdinal -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ParameterUniverseItem<*>
        return ordinal == other.ordinal
    }

    override fun hashCode(): Int {
        return ordinal
    }

    override fun toString() = "type: \"${this::class.simpleName}\", name: \"$name\""
}