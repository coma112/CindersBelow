plugins {
    id("java")
}

group = "net.coma112"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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