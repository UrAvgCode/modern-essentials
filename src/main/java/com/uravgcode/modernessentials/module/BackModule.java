package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.BackEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
@CommandModule(name = "back")
public final class BackModule extends PluginModule {
    private final Map<UUID, Deque<Location>> locations;

    public BackModule(JavaPlugin plugin) {
        super(plugin);
        locations = new ConcurrentHashMap<>();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.COMMAND) return;

        final var player = event.getPlayer();
        final var uuid = player.getUniqueId();

        var deque = locations.computeIfAbsent(uuid, id -> new ArrayDeque<>());
        deque.push(event.getFrom().clone());

        if (deque.size() > 10) {
            deque.removeLast();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBack(BackEvent event) {
        var player = event.getPlayer();
        var stack = locations.get(player.getUniqueId());
        if (stack == null || stack.isEmpty()) return;

        var location = stack.pop();
        player.teleportAsync(location);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        locations.remove(event.getPlayer().getUniqueId());
    }
}
