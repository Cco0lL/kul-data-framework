package kul.dataframework.core

/**
 * @author Cco0lL created 9/16/24 3:57PM
 **/
abstract class AbstractParameter(
    override val ownerContainer: ParameterContainer<*>,
    override val metaData: ParameterMetaData,
): Parameter {

    //TODO: probably it is better to move to separate implementation
    // and call it like a "ObservableAbstractParameter", but not now

    /* ******************* REACTIVITY BEHAVIOR BLOCK ************************ */
    private val changeSubscribers = mutableListOf<ParameterSubscriber>()

    protected fun runSubscribers() {
        for (sub in changeSubscribers)
            sub()
    }

    protected fun allowSubscribe() {
        ownerContainer.atomicSubscriber?.let {
            changeSubscribers += it
        }
    }
    /* ************************************************************************ */

    override fun toString() = "parameter { class: ${this::class.simpleName}, name: ${metaData.key} }"
}