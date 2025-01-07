package kul.dataframework.core.reactive

import kul.dataframework.core.ParameterizedObject
import kul.dataframework.core.read
import java.util.concurrent.locks.ReentrantLock

/**
 * @author Cco0lL created 1/2/25 1:44AM
 **/

/**
 * Represents a file with all core code of reactivity behavior
 * */

//inspired by https://github.com/bendgk/effekt

typealias ParameterSubscriber = () -> Unit

/**
 * the locking mechanism was saved from the original library to avoid some overhead of subscribe
 * lookups of current atomic subscriber, but, unlike the original library, it is possible to make atomic
 * subscriber per parameterized object instance if this part of code became to a bottleneck with negative
 * effects
 */
private val lock = ReentrantLock()

var atomicSubscriber: ParameterSubscriber? = null
    private set

fun <PO : ParameterizedObject> PO.watch(update: PO.() -> Unit) {
    lock.lock()
    try {
        //if lock fence is passed and atomic subscriber is defined then it
        // means inner invocation of watch() function, which is not allowed
        check(atomicSubscriber === null) { "watch() invocation is not allowed in update block" }
        val subscriber: ParameterSubscriber = { update(this) }
        read {
            atomicSubscriber = subscriber
            subscriber()
            atomicSubscriber = null
        }
    } finally {
        lock.unlock()
    }
}


