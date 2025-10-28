package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.manager.CommandManager;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class ModernEssentialsBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
            CommandManager.registerCommands(commands, context.getLogger())
        );
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new ModernEssentials();
    }
}
