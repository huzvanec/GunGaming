plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.shadow)
}

group = "cz.jeme"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.get())
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

val minecraftVersion = libs.versions.paper.get().substringBefore('-')

tasks {
    runServer {
        minecraftVersion(minecraftVersion)
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
