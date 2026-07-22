plugins {
    java
    id("com.gradleup.shadow") version "9.5.1"
    id("org.graalvm.buildtools.native")
}

group = "dev.emortal.minestom.blocksumo"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":core"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

graalvmNative {
    binaries.named("main") {
        imageName.set("blocksumo")
        mainClass.set("dev.emortal.minestom.blocksumo.Main")
    }
}

tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        mergeServiceFiles()

        manifest {
            attributes (
                "Main-Class" to "dev.emortal.minestom.blocksumo.Main",
                "Multi-Release" to true
            )
        }
    }

    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    build {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.isDeprecation = true
    }
}
