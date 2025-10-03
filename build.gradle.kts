import com.vanniktech.maven.publish.SonatypeHost

plugins {
    java
    `java-library`
    alias(libs.plugins.maven.publish)
}

// Function to get version from Git tag
fun getVersionFromTag(): String {
    return try {
        val process = ProcessBuilder("git", "describe", "--tags", "--exact-match", "HEAD")
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        
        val output = process.inputStream.bufferedReader().readText().trim()
        val exitCode = process.waitFor()
        
        if (exitCode == 0 && output.startsWith("v")) {
            output.substring(1) // Remove 'v' prefix
        } else {
            "3.0.0" // fallback version
        }
    } catch (e: Exception) {
        "3.0.0" // fallback version
    }
}

val projectVersion = getVersionFromTag()

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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

    coordinates("nl.bartlouwers", "earcut4j", projectVersion)

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
