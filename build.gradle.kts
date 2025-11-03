import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.run.paper)
}

group = "com.uravgcode"
version = "0.2.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api)
    compileOnly(libs.miniplaceholders)
}

paperPluginYaml {
    main = "com.uravgcode.modernessentials.ModernEssentials"
    bootstrapper = "com.uravgcode.modernessentials.ModernEssentialsBootstrap"
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
