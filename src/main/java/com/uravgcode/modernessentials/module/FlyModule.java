package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.FlyEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@CommandModule(name = "fly")
public final class FlyModule extends PluginModule {
    public final NamespacedKey flyKey;

    public FlyModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        flyKey = new NamespacedKey(plugin, "fly");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFly(FlyEvent event) {
        final var player = event.getPlayer();
        final var dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(flyKey)) {
            disableFlight(player);
            dataContainer.remove(flyKey);
            player.sendMessage(Component.text("Fly mode disabled", NamedTextColor.RED));
        } else {
            enableFlight(player);
            dataContainer.set(flyKey, PersistentDataType.BYTE, (byte) 1);
            player.sendMessage(Component.text("Fly mode enabled", NamedTextColor.GREEN));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        if (shouldEnableFlight(player)) {
            enableFlight(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        final var player = event.getPlayer();
        if (shouldEnableFlight(player)) {
            enableFlight(player);
        }
    }

    @SuppressWarnings("deprecation")
    private void enableFlight(@NotNull final Player player) {
        player.getScheduler().run(plugin, task -> {
            player.setAllowFlight(true);
            if (player.isOnGround()) return;
            player.setFlying(true);
        }, null);
    }

    private void disableFlight(@NotNull Player player) {
        final var gameMode = player.getGameMode();
        if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
            player.setAllowFlight(false);
        }
    }

    private boolean shouldEnableFlight(Player player) {
        return player.getPersistentDataContainer().has(flyKey) && player.hasPermission("essentials.fly");
    }
}
