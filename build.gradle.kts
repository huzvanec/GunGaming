plugins {
    `java-library`
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.shadow)
}

group = "cz.jeme"
version = "1.5.3"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation(libs.classgraph)
    paperweight.paperDevBundle(libs.versions.paper.get())
}

val targetJavaVersion = libs.versions.java.get().toInt()
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

val minecraftVersion = libs.versions.paper.get().substringBefore('-')

tasks {
    runServer {
        minecraftVersion(minecraftVersion)
    }

    withType<JavaCompile> {
        configureEach {
            options.encoding = "UTF-8"

            if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
                options.release = targetJavaVersion
            }
        }
    }

    processResources {
        val props = mapOf(
            "version" to project.version,
            "minecraftVersion" to minecraftVersion
        )
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier = ""
        enableAutoRelocation = true
        relocationPrefix = "cz.jeme.gungaming.shaded"
    }

    assemble { dependsOn(shadowJar) }
}