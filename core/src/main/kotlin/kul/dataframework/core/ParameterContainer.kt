package kul.dataframework.core

/**
 * @author Cco0lL created 9/20/24 3:44AM
 **/
abstract class ParameterContainer<P : Parameter> {

    /**
     * Scopes of the operations can be recursive, but every usage of fences always has effects
     * (if they have effects themselves). The scope counter helps define the primary scope and
     * put the fences only at the entry modify fence
     */
    protected var modifyScopeCounter = 0

    abstract fun <FUN_P : P> get(name: String): FUN_P?

    open fun copy(): ParameterContainer<P> {
        throw UnsupportedOperationException("Not implemented")
    }

    protected open fun handleBeforeModifications() {}
    protected open fun handleAfterModifications() {}

    /**
     * Fences describe the scope of operations that should be happened in a part of code.
     * Explicit fences of read or modify scopes allow to provide special effects when it
     * needed (multithreaded synchronization/handlers/transactional behavior, and e.t.c)
     */

    open fun startModifyFence() {
        if (++modifyScopeCounter == 1) { //entry fence
            try {
                handleAfterModifications()
            } catch (th: Throwable) {
                modifyScopeCounter--
                //rethrowing to interrupt operation scope
                throw th
            }
        }
    }

    open fun stopModifyFence() {
        try {
            if (modifyScopeCounter == 1) { //exit fence
                handleAfterModifications()
            }
        } finally {
            modifyScopeCounter--
        }
    }

    open fun startReadFence() {}
    open fun stopReadFence() {}

}

inline fun <P : Parameter, C : ParameterContainer<P>, R> C.read(readBlock: C.() -> R): R = run {
    startReadFence()
    try {
        readBlock(this)
    } finally {
        stopReadFence()
    }
}

fun <P : Parameter, C : ParameterContainer<P>, R> C.readIt(readBlock: (C) -> R) =
    read(readBlock)

inline fun <P : Parameter, C : ParameterContainer<P>> C.modify(modifyBlock: C.() -> Unit) = apply {
    startModifyFence()
    try {
        modifyBlock(this)
    } finally {
        stopModifyFence()
    }
}

inline fun <P : Parameter, C : ParameterContainer<P>> C.modifyIt(modifyBlock: (C) -> Unit) =
    modify(modifyBlock)