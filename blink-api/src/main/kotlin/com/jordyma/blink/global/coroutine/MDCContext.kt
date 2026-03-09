package com.jordyma.blink.global.coroutine

import kotlinx.coroutines.ThreadContextElement
import org.slf4j.MDC
import kotlin.coroutines.CoroutineContext

/**
 * Coroutine에서 MDC 컨텍스트를 유지하기 위한 CoroutineContext Element
 * - Coroutine이 스레드를 전환해도 MDC 값이 유지됨
 * - withContext, async 등에서 자동으로 MDC 전파
 */
class MDCContext(
    private val contextMap: Map<String, String?> = MDC.getCopyOfContextMap() ?: emptyMap()
) : ThreadContextElement<Map<String, String?>> {

    companion object Key : CoroutineContext.Key<MDCContext>

    override val key: CoroutineContext.Key<MDCContext> = Key

    override fun updateThreadContext(context: CoroutineContext): Map<String, String?> {
        val oldState = MDC.getCopyOfContextMap() ?: emptyMap()
        setCopyOfContextMap(contextMap)
        return oldState
    }

    override fun restoreThreadContext(context: CoroutineContext, oldState: Map<String, String?>) {
        setCopyOfContextMap(oldState)
    }

    private fun setCopyOfContextMap(contextMap: Map<String, String?>) {
        if (contextMap.isEmpty()) {
            MDC.clear()
        } else {
            MDC.setContextMap(contextMap)
        }
    }
}

/**
 * MDC를 포함한 Coroutine Dispatcher
 */
fun withMDCContext(): CoroutineContext {
    return MDCContext()
}
