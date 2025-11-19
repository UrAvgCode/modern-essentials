import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.run.paper)
}

group = "com.uravgcode"
version = "0.4.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
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
    apiVersion = "1.21.10"

    name = "modern-essentials"
    description = "a modern essentials plugin"
    website = "https://uravgcode.com"
    authors.add("UrAvgCode")

    dependencies.server.register("MiniPlaceholders") {
        load = Load.BEFORE
        required = false
        joinClasspath = true
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
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
        minecraftVersion("1.21.10")
    }
}
