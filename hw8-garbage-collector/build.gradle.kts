plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "ru.otus"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.slf4j:slf4j-api:2.0.0")
    implementation("ch.qos.logback:logback-classic:1.2.11") // Logback
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveClassifier.set("")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "ru.calculator.CalcDemo"
        )
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}