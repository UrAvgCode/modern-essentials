package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.command.VanishCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@CommandModule(name = "vanish")
public final class VanishModule extends PluginModule {

    public VanishModule(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        final var onlinePlayers = player.getServer().getOnlinePlayers();

        if (isVanished(player)) {
            event.joinMessage(null);
            player.setInvisible(true);
            onlinePlayers.forEach(other -> {
                other.hidePlayer(plugin, player);
                other.unlistPlayer(player);
            });
        } else {
            player.setInvisible(false);
            onlinePlayers.forEach(other -> {
                if (isVanished(other)) {
                    player.hidePlayer(plugin, other);
                    player.unlistPlayer(other);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isVanished(event.getPlayer())) {
            event.quitMessage(null);
        }
    }

    private boolean isVanished(Player player) {
        return player.getPersistentDataContainer().has(VanishCommand.VANISH_KEY) && player.hasPermission("essentials.vanish");
    }
}
