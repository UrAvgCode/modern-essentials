package com.uravgcode.modernessentials.listener;

import com.uravgcode.modernessentials.command.VanishCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public record VanishListener(@NotNull JavaPlugin plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (isVanished(player)) {
            event.joinMessage(null);
            for (var other : player.getServer().getOnlinePlayers()) {
                other.hidePlayer(plugin, player);
                other.unlistPlayer(player);
            }
        } else {
            for (var other : player.getServer().getOnlinePlayers()) {
                if (isVanished(other)) {
                    player.hidePlayer(plugin, other);
                    player.unlistPlayer(other);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoinGod(PlayerQuitEvent event) {
        if (isVanished(event.getPlayer())) {
            event.quitMessage(null);
        }
    }

    private boolean isVanished(Player player) {
        return player.getPersistentDataContainer().has(VanishCommand.VANISH_KEY) && player.hasPermission("essentials.vanish");
    }
}
