plugins {
    java
    `java-library`
}

group = "io.github.earcut4j"
version = "2.2.3-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.bundles.test)
}

tasks.test {
    useJUnitPlatform()
}

tasks.javadoc {
    options {
        source = "8"
        (this as StandardJavadocDocletOptions).addBooleanOption("detectJavaApiLink", false)
    }
}