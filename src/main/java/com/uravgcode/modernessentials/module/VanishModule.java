package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.VanishEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@CommandModule(name = "vanish")
public final class VanishModule extends PluginModule {
    private final NamespacedKey vanishKey;

    public VanishModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        vanishKey = new NamespacedKey(plugin, "vanish");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVanish(VanishEvent event) {
        final var player = event.getPlayer();
        final var dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(vanishKey)) {
            showPlayer(player);
            dataContainer.remove(vanishKey);
            player.sendMessage(Component.text("Vanish mode disabled", NamedTextColor.RED));
        } else {
            hidePlayer(player);
            dataContainer.set(vanishKey, PersistentDataType.BYTE, (byte) 1);
            player.sendMessage(Component.text("Vanish mode enabled", NamedTextColor.GREEN));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();

        if (isVanished(player)) {
            hidePlayer(player);
            event.joinMessage(null);
        } else {
            hideVanishedPlayers(player);
            player.setInvisible(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isVanished(event.getPlayer())) {
            event.quitMessage(null);
        }
    }

    private void hideVanishedPlayers(@NotNull Player player) {
        for (final var other : player.getServer().getOnlinePlayers()) {
            if (isVanished(other)) {
                player.hidePlayer(plugin, other);
                player.unlistPlayer(other);
            }
        }
    }

    private void hidePlayer(@NotNull Player player) {
        player.setInvisible(true);
        for (final var other : player.getServer().getOnlinePlayers()) {
            other.hidePlayer(plugin, player);
            other.unlistPlayer(player);
        }
    }

    private void showPlayer(@NotNull Player player) {
        player.setInvisible(false);
        for (final var other : player.getServer().getOnlinePlayers()) {
            other.showPlayer(plugin, player);
            other.listPlayer(player);
        }
    }

    private boolean isVanished(@NotNull Player player) {
        return player.getPersistentDataContainer().has(vanishKey) && player.hasPermission("essentials.vanish");
    }
}
