package kul.dataframework.core

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantReadWriteLock

typealias ParameterSubscriber = () -> Unit

/**
 * @author Cco0lL created 9/20/24 3:44AM
 **/
abstract class ParameterContainer<P : Parameter>(
    protected val rootContainer: ParameterContainer<*>?
) : Iterable<P> {

    private val rwLock = ReentrantReadWriteLock(true)

    private val acquireModifyAdder = AtomicInteger()
    private val acquireReadAdder = AtomicInteger()

    var isModifyingNow: Boolean = false
        get() = rootContainer?.run { isModifyingNow } ?: false || acquireModifyAdder.get() != 0
        private set
    var isReadingNow: Boolean = false
        get() = rootContainer?.run { isReadingNow } ?: false || acquireReadAdder.get() != 0
        private set

    @Volatile
    internal var atomicSubscriber: ParameterSubscriber? = null

    open fun handleBeforeModifications() {}
    open fun handleAfterModifications() {}

    abstract fun get(name: String): P

    // inspired by https://github.com/bendgk/effekt
    fun subscribe(sub: ParameterSubscriber) {
        check(atomicSubscriber == null) {
            "can't invoke ParameterContainer#subscribe() inside another subscribe block"
        }
//        check(!(isModifyingNow || isReadingNow)) {
//            "can't invoke ParameterContainer#subscribe() because reading or modifying block are running"
//        }
        rwLock.writeLock().lock()
        atomicSubscriber = sub
        try {
            sub()
        } finally {
            atomicSubscriber = null
            rwLock.writeLock().unlock()
        }
    }

    fun enableModifications() {
//        if (isModifyingNow) {
//            throw IllegalStateException("redundant ModifiableItem#enableModifications() call")
//        }

        rwLock.writeLock().lock()
        acquireModifyAdder.incrementAndGet()
        try {
            handleBeforeModifications()
        } catch (th: Throwable) {
            acquireModifyAdder.decrementAndGet()
            rwLock.writeLock().unlock()
            throw th
        }
    }

    fun disableModifications() {
//        if (!isModifyingNow) {
//            throw IllegalStateException("redundant ModifiableItem#disableModifications() call")
//        }

//        isModifyingNow = false

        try {
            handleAfterModifications()
        } finally {
            acquireModifyAdder.decrementAndGet()
            rwLock.writeLock().unlock()
        }
    }

    fun acquireRead() {
//        isReadingNow = true
        rwLock.readLock().lock()
        acquireReadAdder.incrementAndGet()
    }

    fun releaseRead() {
        acquireReadAdder.decrementAndGet()
        rwLock.readLock().unlock()
//        isReadingNow = false
    }

    fun checkIsModificationsEnabled() {
        if (!isModifyingNow)
            throw IllegalStateException("Attempt to modify parameter container with disabled modifications")
    }
}

inline fun <P : Parameter, C : ParameterContainer<P>, R> C.acquireRead(readBlock: C.() -> R): R = run {
    if (isModifyingNow || isReadingNow) {
        //don't need to acquire read in modify block or when it is a nested block
        readBlock(this)
    } else {
        acquireRead()
        try {
            readBlock(this)
        } finally {
            releaseRead()
        }
    }
}

fun <P : Parameter, C : ParameterContainer<P>, R> C.acquireReadIt(readBlock: (C) -> R) =
    acquireRead(readBlock)

inline fun <P : Parameter, C : ParameterContainer<P>> C.modify(modifyBlock: C.() -> Unit) = apply {
//    if (isReadingNow) {
//        throw IllegalArgumentException("Can't be modified while read has been acquired")
//    }
    if (isModifyingNow) {
       //nested block, passing without preparations and special code
       modifyBlock(this)
    } else {
        enableModifications()
        try {
            modifyBlock(this)
        } finally {
            disableModifications()
        }
    }
}

inline fun <P : Parameter, C : ParameterContainer<P>> C.modifyIt(modifyBlock: (C) -> Unit) =
    modify(modifyBlock)