plugins {
    kotlin("jvm") version "2.2.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.0.1"
}

group = "cz.jeme"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation("io.github.classgraph:classgraph:4.8.181")
    implementation("org.spongepowered:configurate-hocon:4.2.0")
    implementation("org.spongepowered:configurate-extra-kotlin:4.2.0")
    paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks {
    withType<JavaCompile> {
        configureEach {
            options.encoding = "UTF-8"

            if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
                options.release = targetJavaVersion
            }
        }
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    runServer {
        minecraftVersion("1.21.5")
    }

    shadowJar {
        archiveClassifier = ""
        enableRelocation = true
        relocationPrefix = "${project.group}.${project.name.lowercase()}.shaded"

        dependencies {
            exclude(dependency("org.jetbrains:annotations:.*"))
        }
    }

    assemble { dependsOn(shadowJar) }
}