package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.GodEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@CommandModule(name = "god")
public final class GodModule extends PluginModule {
    private final NamespacedKey godKey;

    public GodModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        godKey = new NamespacedKey(plugin, "god");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onGod(GodEvent event) {
        final var player = event.getPlayer();
        final var dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(godKey)) {
            dataContainer.remove(godKey);
            player.sendMessage(Component.text("God mode disabled", NamedTextColor.RED));
        } else {
            player.setHealth(20);
            player.setFoodLevel(20);
            dataContainer.set(godKey, PersistentDataType.BYTE, (byte) 1);
            player.sendMessage(Component.text("God mode enabled", NamedTextColor.GREEN));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof HumanEntity player && isGod(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (isGod(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private boolean isGod(@NotNull HumanEntity player) {
        return player.getPersistentDataContainer().has(godKey) && player.hasPermission("essentials.god");
    }
}
