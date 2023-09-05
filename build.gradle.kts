plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.15.0"
}

group = "ski.mashiro"
version = "3.0.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

mirai {
    jvmTarget = JavaVersion.VERSION_1_8
}

dependencies {
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.13.0")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}