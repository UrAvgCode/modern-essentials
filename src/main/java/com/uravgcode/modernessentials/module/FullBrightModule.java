package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.FullBrightEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

@CommandModule(name = "fullbright")
public final class FullBrightModule extends PluginModule {
    public final NamespacedKey fullBrightKey;

    public FullBrightModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        fullBrightKey = new NamespacedKey(plugin, "fullbright");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFullBright(FullBrightEvent event) {
        final var player = event.getPlayer();
        final var dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(fullBrightKey)) {
            dataContainer.remove(fullBrightKey);
            player.sendPotionEffectChangeRemove(player, PotionEffectType.NIGHT_VISION);
            player.sendMessage(Component.text("Fullbright disabled", NamedTextColor.RED));
        } else {
            dataContainer.set(fullBrightKey, PersistentDataType.BYTE, (byte) 1);
            applyFullBright(event.getPlayer());
            player.sendMessage(Component.text("Fullbright enabled", NamedTextColor.GREEN));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        applyFullBright(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        applyFullBright(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        applyFullBright(event.getPlayer());
    }

    private void applyFullBright(@NotNull Player player) {
        if (!shouldEnableFullBright(player)) return;
        final var effect = new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false, false);
        player.getScheduler().run(plugin, task -> player.sendPotionEffectChange(player, effect), null);
    }

    private boolean shouldEnableFullBright(@NotNull Player player) {
        return player.getPersistentDataContainer().has(fullBrightKey) && player.hasPermission("essentials.fullbright");
    }
}
