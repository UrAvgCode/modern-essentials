package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.command.*;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ModernEssentialsBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            var registrar = commands.registrar();
            EssentialsCommand.registerCommands(registrar);
            WorkstationCommand.registerCommands(registrar);
            EnderChestCommand.registerCommands(registrar);
            InventorySeeCommand.registerCommands(registrar);
            TimeCommand.registerCommands(registrar);
            WeatherCommand.registerCommands(registrar);
            GodCommand.registerCommands(registrar);
            FlyCommand.registerCommands(registrar);
        });
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new ModernEssentials();
    }
}
