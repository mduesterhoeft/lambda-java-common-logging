# lambda-java-common-logging

Logging library enforcing common conventions for logging in AWS lambda.

It is based on `slf4j` and uses the `log4j` binding because the AWS Java SDK is logging using `log4j`.

The logstash encoder is used to log json messages that can easily be indexed by Elasticsearch.

This library makes sure we produce similar log messages by enforcing conventional json attribute names.


The following code examples produce the same log message shown below:

```kotlin
LoggingContext
    .setTraceId("Trace-id")
    .setClientIdentifier("Moia-client-identifier")
    .setRequestId("request-id")
    .setComponent("some")

log.info("Test", dataContent(MyContent("1", false)))
```

```kotlin
LoggingContext
    .populateMdcFromHeaders(mapOf(
        "Trace-id" to "Trace-id",
        "Moia-client-identifier" to "Moia-client-identifier"))
    .setRequestId("request-id")
    .setComponent("some")

log.info("Test", dataContent(MyContent("1", false)))
```

```json
{"timestamp":"2019-03-18T21:38:41.463Z","logger":"i.m.l.logging.LoggingExtensionTest","level":"INFO","traceId":"Trace-id","component":"some","requestId":"request-id","clientIdentifier":"Moia-client-identifier","dataContent":{"test":"1","test2":false},"message":"Test"}
```
