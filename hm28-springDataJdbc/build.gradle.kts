plugins {
    id("org.springframework.boot") version "3.3.1" // Указана версия Spring Boot
    id("io.spring.dependency-management") // Убираем явную версию плагина, так как она уже в classpath
    kotlin("jvm") version "1.8.21" // Указываем версию Kotlin
    id("org.flywaydb.flyway") version "9.22.1" // Указана версия Flyway
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
    implementation("org.postgresql:postgresql:42.6.0") // Указана последняя стабильная версия PostgreSQL

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

task("wrapper", type = Wrapper::class) {
    gradleVersion = "8.8" // Указание версии Gradle
    distributionType = Wrapper.DistributionType.ALL
}

flyway {
    url = "jdbc:postgresql://localhost:5430/demoDB?currentSchema=my_schema"
    user = "usr"
    password = "pwd"
    locations = arrayOf("classpath:db/migration") // Путь до папки миграций
    schemas = arrayOf("my_schema") // Указываем схему для миграций
    cleanDisabled = false // Разрешаем выполнение команды clean
    baselineOnMigrate = true // Включаем базовую миграцию
}
