import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "1.9.10"
    id("com.google.protobuf") version "0.9.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.grpc:grpc-netty:1.56.1")
    implementation("io.grpc:grpc-protobuf:1.56.1")
    implementation("io.grpc:grpc-stub:1.56.1")
    implementation("com.google.protobuf:protobuf-java:3.24.3")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation(kotlin("stdlib"))
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.3"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.56.1"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc")
            }
        }
    }
}

kotlin {
    jvmToolchain(17) // Используем Java 17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

tasks.named<Copy>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("**/numbers.proto")
}

sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
    }
}
