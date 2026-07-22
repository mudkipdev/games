plugins {
    java
    application
    id("com.gradleup.shadow") version "9.5.1"
    id("org.graalvm.buildtools.native")
}

group = "dev.emortal.minestom"
version = "1.0-SNAPSHOT"
application.mainClass = "dev.emortal.minestom.marathon.Main"

dependencies {
    implementation(project(":core"))
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.9")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        mergeServiceFiles()

        manifest {
            attributes(
                "Multi-Release" to true
            )
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.isDeprecation = true
    }

    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    build {
        dependsOn(shadowJar)
    }
}

graalvmNative {
    binaries.named("main") {
        imageName.set("marathon")
        mainClass.set(application.mainClass)
    }
}