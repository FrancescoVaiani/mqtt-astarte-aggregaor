plugins {
    id("java")
}

group = "com.secomind.poc.aggregator.aggregator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    // https://mvnrepository.com/artifact/org.astarte-platform/devicesdk-generic
    implementation("org.astarte-platform:devicesdk-generic:1.0.3")
    implementation("com.h2database:h2:1.4.200")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
