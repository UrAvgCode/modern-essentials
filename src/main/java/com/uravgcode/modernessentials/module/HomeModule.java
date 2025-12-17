package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.home.DelHomeEvent;
import com.uravgcode.modernessentials.event.home.HomeEvent;
import com.uravgcode.modernessentials.event.home.SetHomeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@NullMarked
@CommandModule(name = "home")
public final class HomeModule extends PluginModule {
    private static final Map<UUID, NavigableMap<String, Location>> homes = new ConcurrentHashMap<>();
    private final File file;

    public HomeModule(JavaPlugin plugin) {
        super(plugin);
        this.file = plugin.getDataPath().resolve("homes.yml").toFile();
    }

    public static NavigableMap<String, Location> getHomes(UUID uuid) {
        final var playerHomes = homes.get(uuid);
        return playerHomes == null
            ? Collections.emptyNavigableMap()
            : Collections.unmodifiableNavigableMap(playerHomes);
    }

    @Override
    public void reload() {
        super.reload();

        homes.clear();
        final var logger = plugin.getComponentLogger();
        final var config = YamlConfiguration.loadConfiguration(file);

        for (final var uuidString : config.getKeys(false)) {
            try {
                final var uuid = UUID.fromString(uuidString);

                final var section = config.getConfigurationSection(uuidString);
                if (section == null) continue;

                final var playerHomes = new ConcurrentSkipListMap<String, Location>(String.CASE_INSENSITIVE_ORDER);
                for (final var name : section.getKeys(false)) {
                    final var location = section.getLocation(name);
                    if (location != null) {
                        playerHomes.put(name, location);
                    } else {
                        logger.warn("Home '{}' for player {} does not contain a valid location", name, uuidString);
                    }
                }

                homes.put(uuid, playerHomes);
            } catch (IllegalArgumentException exception) {
                logger.warn("Invalid UUID in homes.yml: {}", uuidString);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHome(HomeEvent event) {
        final var player = event.getPlayer();
        final var name = event.getName();
        final var playerHomes = homes.get(player.getUniqueId());

        if (playerHomes == null) {
            player.sendMessage(Component.text("Home not found", NamedTextColor.RED));
            return;
        }

        final var location = playerHomes.get(name);
        if (location != null) {
            player.teleportAsync(location, TeleportCause.COMMAND);
        } else {
            player.sendMessage(Component.text("Home not found", NamedTextColor.RED));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSetHome(SetHomeEvent event) {
        final var player = event.getPlayer();
        final var name = event.getName();
        final var location = player.getLocation();

        final var playerHomes = homes.computeIfAbsent(player.getUniqueId(), uuid -> new ConcurrentSkipListMap<>(String.CASE_INSENSITIVE_ORDER));
        if (playerHomes.put(name, location) == null) {
            player.sendMessage("Home created");
        } else {
            player.sendMessage("Home set");
        }

        saveHomes();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDelHome(DelHomeEvent event) {
        final var player = event.getPlayer();
        final var name = event.getName();

        final var playerHomes = homes.get(player.getUniqueId());
        if (playerHomes != null && playerHomes.remove(name) != null) {
            player.sendMessage("Home deleted");
            saveHomes();
        } else {
            player.sendMessage(Component.text("Home not found", NamedTextColor.RED));
        }
    }

    private void saveHomes() {
        final var snapshot = Map.copyOf(homes);
        CompletableFuture.runAsync(() -> {
            try {
                final var config = new YamlConfiguration();
                snapshot.forEach((uuid, playerHomes) -> {
                    final var playerSection = config.createSection(uuid.toString());
                    playerHomes.forEach(playerSection::set);
                });
                config.save(file);
            } catch (IOException exception) {
                plugin.getComponentLogger().error("Could not save homes.yml", exception);
            }
        });
    }
}
