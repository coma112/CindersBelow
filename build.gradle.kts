plugins {
    id("java")
    id("io.freefair.lombok") version "8.11"
}

group = "net.coma112"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.36")
    implementation("org.apache.logging.log4j:log4j-core:2.25.2")
    implementation("org.jetbrains:annotations:26.0.2-1")
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}