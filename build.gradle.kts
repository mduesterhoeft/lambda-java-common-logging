plugins {
    java
    kotlin("jvm") version "1.3.21"
    jacoco
    id("org.jmailen.kotlinter") version "1.21.0"
    maven
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.slf4j:log4j-over-slf4j:1.7.26")
    implementation("net.logstash.logback:logstash-logback-encoder:5.3")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.0")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("com.jayway.jsonpath:json-path:2.4.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}