package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.command.GodCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class GodModule extends PluginModule {

    public GodModule(@NotNull JavaPlugin plugin) {
        super(plugin);
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

    private boolean isGod(HumanEntity player) {
        return player.getPersistentDataContainer().has(GodCommand.GOD_KEY) && player.hasPermission("essentials.god");
    }
}
