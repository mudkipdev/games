plugins {
    java
    id("com.gradleup.shadow") version "9.5.1"
    id("org.graalvm.buildtools.native")
}

group = "dev.emortal.minestom"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
}

dependencies {
    implementation(project(":core"))

    implementation("com.alibaba.fastjson2:fastjson2:2.0.61")
    implementation("dev.emortal:bbstom:local")
    implementation("org.joml:joml:1.10.8")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(25))

graalvmNative {
    binaries.named("main") {
        imageName.set("lobby")
        mainClass.set("dev.emortal.minestom.lobby.Entrypoint")
    }
}

tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        mergeServiceFiles()

        manifest {
            attributes(
                "Main-Class" to "dev.emortal.minestom.lobby.Entrypoint",
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
