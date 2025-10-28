package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.command.FlyCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@CommandModule(name = "fly")
public final class FlyModule extends PluginModule {

    public FlyModule(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (shouldEnableFlight(player)) {
            enableFlight(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        var player = event.getPlayer();
        if (shouldEnableFlight(player)) {
            enableFlight(player);
        }
    }

    private void enableFlight(Player player) {
        player.getScheduler().run(plugin, task -> {
            player.setAllowFlight(true);
            player.setFlying(true);
        }, null);
    }

    private boolean shouldEnableFlight(Player player) {
        return player.getPersistentDataContainer().has(FlyCommand.FLY_KEY) && player.hasPermission("essentials.fly");
    }
}
