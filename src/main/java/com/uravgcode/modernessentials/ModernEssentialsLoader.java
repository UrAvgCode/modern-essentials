package com.uravgcode.modernessentials;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class ModernEssentialsLoader implements PluginLoader {

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        final var resolver = new MavenLibraryResolver();
        resolver.addRepository(new RemoteRepository.Builder("codemc-releases", "default", "https://repo.codemc.io/repository/maven-releases/").build());
        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.retrooper:packetevents-spigot:2.11.0"), null));
        classpathBuilder.addLibrary(resolver);
    }
}
