import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class MetricsCollector(
    private val meterRegistry: MeterRegistry
) {
    private val activeRequests = AtomicInteger(0)
    private val queuedRequests = AtomicInteger(0)

    init {
        Gauge.builder("summarization.active.requests", activeRequests::get)
            .description("현재 처리 중인 요약 요청 수")
            .register(meterRegistry)

        Gauge.builder("summarization.queued.requests", queuedRequests::get)
            .description("대기열에 쌓인 요청 수")
            .register(meterRegistry)
    }

    fun incrementActive() = activeRequests.incrementAndGet()
    fun decrementActive() = activeRequests.decrementAndGet()
    fun incrementQueued() = queuedRequests.incrementAndGet()
    fun decrementQueued() = queuedRequests.decrementAndGet()
}