plugins {
    id("java")
    id("application")
}

group = "bontail"
version = "1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("server.MainServer")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("ch.qos.logback:logback-core:1.5.13")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.18.3")
    implementation("org.jetbrains:annotations:26.0.2")
}

tasks.register<Jar>("serverFatJar") {
    archiveClassifier.set("server")
    manifest {
        attributes["Main-Class"] = "server.MainServer"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


tasks.register<Jar>("clientFatJar") {
    archiveClassifier.set("client")
    manifest {
        attributes["Main-Class"] = "client.Client"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.build {
    dependsOn("serverFatJar", "clientFatJar")
}