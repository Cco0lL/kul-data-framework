package kul.dataframework.core

import java.lang.ref.WeakReference

/**
 * @author Cco0lL created 9/16/24 3:57PM
 **/
abstract class AbstractParameter(
    ownerContainer: ParameterContainer<*>,
    override val metaData: ParameterMetaData,
): Parameter {

    override val ownerContainer: ParameterContainer<*> get() {
        val ownerContainer = ownerContainerWeakRef.get()
        if (ownerContainer === null) {
            throw IllegalStateException("Parameter's lifecycle out of beyond container's lifecycle")
        }
        return ownerContainer
    }

    private val ownerContainerWeakRef = WeakReference(ownerContainer)

    /* ******************* REACTIVITY BEHAVIOR BLOCK ************************ */
/*    private val changeSubscribers = mutableListOf<ParameterSubscriber>()

    protected fun runSubscribers() {
        for (sub in changeSubscribers)
            sub()
    }

    protected fun allowSubscribe() {
        val ownerContainer = ownerContainer
        // it is better to make another implementation of observable parameter but rn it is just an experimental
        // feature and I don't want to make another implementation
        if (ownerContainer is ParameterizedObject) {
            ownerContainer.atomicSubscriber?.let {
                changeSubscribers += it
            }
        }
    } */
    /* ************************************************************************ */

    override fun toString() = "parameter { class: ${this::class.simpleName}, name: ${metaData.key} }"
}