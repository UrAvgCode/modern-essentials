import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.run.paper)
}

group = "com.uravgcode"
version = "0.1.0"

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api)
    compileOnly(libs.miniplaceholders)
}

tasks {
    runServer {
        minecraftVersion("1.21.10")
    }
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

paperPluginYaml {
    main = "com.uravgcode.modernessentials.ModernEssentials"
    bootstrapper = "com.uravgcode.modernessentials.ModernEssentialsBootstrap"
    foliaSupported = true
    apiVersion = "1.21"

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
