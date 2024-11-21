plugins {
    id("org.springframework.boot") // Версия берётся из корневого проекта (3.3.1)
    id("io.spring.dependency-management") // Версия берётся из корневого проекта
    kotlin("jvm") version "1.8.21" // Указываем версию Kotlin
}

group = "ru.otus"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral() // Используем центральный Maven-репозиторий
}

dependencies {
    // Зависимости для Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web") // Для MVC
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf") // Для Thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc") // Для Spring Data JDBC

    // Зависимость для работы с PostgreSQL
    implementation("org.postgresql:postgresql:42.6.0") // Указана последняя стабильная версия

    // Для управления миграциями базы данных
    implementation("org.flywaydb:flyway-core:9.22.1") // Указана последняя версия Flyway

    // Зависимости для тестирования
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine") // Исключаем старый JUnit 4
    }
}

tasks.test {
    useJUnitPlatform() // Указываем JUnit Platform для тестов
}
