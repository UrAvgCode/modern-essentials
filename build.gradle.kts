import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.run.paper)
}

group = "com.uravgcode"
version = "0.7.1"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
    maven(uri("https://repo.codemc.io/repository/maven-releases/"))
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api)
    compileOnly(libs.packetevents)
    compileOnly(libs.miniplaceholders)
}

paperPluginYaml {
    main = "com.uravgcode.modernessentials.ModernEssentials"
    bootstrapper = "com.uravgcode.modernessentials.ModernEssentialsBootstrap"
    loader = "com.uravgcode.modernessentials.ModernEssentialsLoader"
    foliaSupported = true
    apiVersion = "26.1"

    name = "modern-essentials"
    description = "a modern essentials plugin"
    website = "https://uravgcode.com"
    authors.add("UrAvgCode")

    dependencies.server.register("MiniPlaceholders") {
        load = Load.BEFORE
        required = false
        joinClasspath = true
    }

    dependencies.server.register("ViaVersion") {
        load = Load.AFTER
        required = false
        joinClasspath = false
    }
}

runPaper {
    folia.registerTask()
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(25)
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("config.yml") {
            expand(props)
        }
    }

    runServer {
        minecraftVersion("26.2")
    }
}
