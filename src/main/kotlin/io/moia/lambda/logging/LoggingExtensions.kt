package io.moia.lambda.logging

import net.logstash.logback.argument.StructuredArguments
import org.slf4j.MDC

object LoggingContext {
    const val traceIdMdcName = "traceId"
    const val requestIdMdcName = "requestId"
    const val clientIdentifierMdcName = "clientIdentifier"
    const val componentMdcName = "component"

    const val traceIdHeaderName = "trace-id"
    const val clientIdentifierHeaderName = "moia-client-identifier"

    fun populateMdcFromHeaders(headers: Map<String, Any>): LoggingContext {
        MDC.put(traceIdMdcName, headers.getCaseInsensitive(traceIdHeaderName) ?: "none")
        MDC.put(clientIdentifierMdcName, headers.getCaseInsensitive(clientIdentifierHeaderName) ?: "none")
        return this
    }

    fun setRequestId(requestId: String?): LoggingContext {
        MDC.put(requestIdMdcName, requestId ?: "none")
        return this
    }

    fun setClientIdentifier(clientIdentifier: String?): LoggingContext {
        MDC.put(clientIdentifierMdcName, clientIdentifier ?: "none")
        return this
    }

    fun setTraceId(traceId: String?): LoggingContext {
        MDC.put(traceIdMdcName, traceId ?: "none")
        return this
    }

    fun setComponent(component: String): LoggingContext {
        MDC.put(componentMdcName, component)
        return this
    }

    private fun Map<String, Any>.getCaseInsensitive(key: String): String? = entries.firstOrNull { it.key.toLowerCase() == key.toLowerCase() }?.value as? String
}

object DataContentStructuredArgument {

    private const val dataContentName = "dataContent"

    fun dataContent(value: Any) = StructuredArguments.value(dataContentName, value)
}
