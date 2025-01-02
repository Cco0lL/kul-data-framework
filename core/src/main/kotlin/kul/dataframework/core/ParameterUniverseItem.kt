package kul.dataframework.core

open class ParameterUniverseItem<P : Parameter, M : ParameterMetaData>(
    val metaData: M,
    val creator: (M) -> P
): Comparable<ParameterUniverseItem<*, *>> {

    var ordinal = -1
        private set

    internal fun _setOrdinal(value: Int) {
        check(ordinal == -1) { "can't set ordinal since it already in universe" }
        ordinal = value
    }

    fun createParam() = creator(metaData)

    override fun compareTo(other: ParameterUniverseItem<*, *>): Int {
        val ordinal = ordinal
        val otherOrdinal = other.ordinal
        return when {
            ordinal > otherOrdinal -> 1
            ordinal < otherOrdinal -> -1
            else -> 0
        }
    }
}