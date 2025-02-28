package kul.dataframework.core

import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * @author Cco0lL created 1/1/25 11:45PM
 **/
open class ConcurrentParameterizedObject : ParameterizedObject() {

    private val rwLock = ReentrantReadWriteLock(true)

    override fun startModifyFence() {
        rwLock.writeLock().lock()
        if (++modifyScopeCounter == 1) { //entry fence
            try {
                handleBeforeModifications()
            } catch (th: Throwable) {
                modifyScopeCounter--
                rwLock.writeLock().unlock()
                //rethrowing to interrupt operation scope
                throw th
            }
        }
    }

    override fun stopModifyFence() {
        try {
            if (modifyScopeCounter == 1) { //exit fence
                handleAfterModifications()
            }
        } finally {
            modifyScopeCounter--
            rwLock.writeLock().unlock()
        }
    }

    override fun startReadFence() {
        rwLock.readLock().lock()
    }

    override fun stopReadFence() {
        rwLock.readLock().unlock()
    }
}