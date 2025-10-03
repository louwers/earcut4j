import com.vanniktech.maven.publish.SonatypeHost

plugins {
    java
    `java-library`
    alias(libs.plugins.maven.publish)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("nl.bartlouwers", "earcut4j", "3.0.0")

    pom {
        name = "earcut4j"
        description = "A Java port of the earcut polygon triangulation library, based on the JavaScript version from @mapbox/earcut"
        inceptionYear = "2020"
        url = "https://github.com/louwers/earcut4j/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "louwers"
                name = "Bart Louwers"
                url = "https://github.com/louwers/"
            }
        }
        scm {
            url = "https://github.com/louwers/earcut4j/"
            connection = "scm:git:git://github.com/louwers/earcut4j.git"
            developerConnection = "scm:git:ssh://git@github.com/louwers/earcut4j.git"
        }
    }
}
