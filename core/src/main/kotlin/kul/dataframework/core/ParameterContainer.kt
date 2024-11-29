package kul.dataframework.core

import java.util.concurrent.locks.ReentrantReadWriteLock

typealias ParameterSubscriber = () -> Unit

/**
 * @author Cco0lL created 9/20/24 3:44AM
 **/
abstract class ParameterContainer<P : Parameter>(
    protected val rootContainer: ParameterContainer<*>?
) : Iterable<P> {

    private val rwLock = ReentrantReadWriteLock()

    @Volatile
    var isModifyingNow: Boolean = false
        get() = rootContainer?.run { isModifyingNow } ?: false || field
        private set
    @Volatile
    var isReadingNow: Boolean = false
        get() = rootContainer?.run { isReadingNow } ?: false || field
        private set

    internal var atomicSubscriber: ParameterSubscriber? = null

    open fun handleBeforeModifications() {}
    open fun handleAfterModifications() {}

    abstract fun get(name: String): P

    // inspired by https://github.com/bendgk/effekt
    fun subscribe(sub: ParameterSubscriber) {
        atomicSubscriber = sub
        sub()
        atomicSubscriber = null
    }

    fun enableModifications() {
        if (isModifyingNow) {
            throw IllegalStateException("redundant ModifiableItem#enableModifications() call")
        }

        rwLock.writeLock().lock()
        isModifyingNow = true

        try {
            handleBeforeModifications()
        } catch (th: Throwable) {
            rwLock.writeLock().unlock()
            throw th
        }
    }

    fun disableModifications() {
        if (!isModifyingNow) {
            throw IllegalStateException("redundant ModifiableItem#disableModifications() call")
        }

        isModifyingNow = false

        try {
            handleAfterModifications()
        } finally {
            rwLock.writeLock().unlock()
        }
    }

    fun acquireRead() {
        isReadingNow = true
        rwLock.readLock().lock()
    }

    fun releaseRead() {
        rwLock.readLock().unlock()
        isReadingNow = false
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
    if (isReadingNow) {
        throw IllegalArgumentException("Can't be modified while read has been acquired")
    }
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