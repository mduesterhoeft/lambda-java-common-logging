package io.moia.lambda.logging

import com.jayway.jsonpath.JsonPath
import io.moia.lambda.logging.DataContentStructuredArgument.dataContent
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream

class LoggingExtensionTest {
    val log = LoggerFactory.getLogger(LoggingExtensionTest::class.java)

    lateinit var consoleOutput: OutputStream
    lateinit var originalOutput: PrintStream

    @BeforeEach
    fun setup() {
        redirectConsole()
        MDC.clear()
    }

    @AfterEach
    fun after() {
        System.setOut(originalOutput)
        println(consoleOutput.toString())
    }

    @Test
    fun `should log json`() {
        LoggingContext
            .populateMdcFromHeaders(mapOf(
                "Trace-id" to "Trace-id",
                "Moia-client-identifier" to "Moia-client-identifier"))
            .setRequestId("request-id")
            .setComponent("some")
        log.info("Test")

        val logLine = consoleOutput.toString().lines().first()
        then(JsonPath.read<String>(logLine, "timestamp")).isNotEmpty()
        then(JsonPath.read<String>(logLine, "message")).isEqualTo("Test")
        then(JsonPath.read<String>(logLine, "traceId")).isEqualTo("Trace-id")
        then(JsonPath.read<String>(logLine, "clientIdentifier")).isEqualTo("Moia-client-identifier")
        then(JsonPath.read<String>(logLine, "requestId")).isEqualTo("request-id")
        then(JsonPath.read<String>(logLine, "logger")).isNotEmpty()
        then(JsonPath.read<String>(logLine, "level")).isEqualTo("INFO")
        then(JsonPath.read<String>(logLine, "component")).isEqualTo("some")
    }

    @Test
    fun `should log data content`() {
        LoggingContext
            .setTraceId("Trace-id")
            .setClientIdentifier("Moia-client-identifier")
            .setRequestId("request-id")
            .setComponent("some")

        log.info("Test", dataContent(MyContent("1", false)))

        val logLine = consoleOutput.toString().lines().first()
        then(JsonPath.read<String>(logLine, "dataContent.test")).isNotEmpty()
        then(JsonPath.read<Boolean>(logLine, "dataContent.test2")).isFalse()
        then(JsonPath.read<String>(logLine, "clientIdentifier")).isEqualTo("Moia-client-identifier")
        then(JsonPath.read<String>(logLine, "traceId")).isEqualTo("Trace-id")
    }

    data class MyContent(val test: String, val test2: Boolean)

    private fun redirectConsole() {
        originalOutput = System.out
        consoleOutput = ByteArrayOutputStream()
        System.setOut(PrintStream(consoleOutput))
    }
}