package com.uravgcode.modernessentials;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ModernEssentialsBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new ModernEssentials();
    }
}
