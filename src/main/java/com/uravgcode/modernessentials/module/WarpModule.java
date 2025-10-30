package com.uravgcode.modernessentials.module;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.warp.DelWarpEvent;
import com.uravgcode.modernessentials.event.warp.SetWarpEvent;
import com.uravgcode.modernessentials.event.warp.WarpEvent;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

@CommandModule(name = "warp")
public final class WarpModule extends PluginModule {
    private static final TreeMap<String, Location> warps = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final File file;

    public WarpModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        this.file = plugin.getDataPath().resolve("warps.yml").toFile();
    }

    public static CompletableFuture<Suggestions> suggestions(CommandContext<CommandSourceStack> ignored, SuggestionsBuilder builder) {
        final var remaining = builder.getRemainingLowerCase();
        for (final var warp : warps.tailMap(remaining).keySet()) {
            if (!warp.toLowerCase().startsWith(remaining)) break;
            builder.suggest(warp);
        }
        return builder.buildFuture();
    }

    @Override
    public void reload() {
        super.reload();

        warps.clear();
        final var logger = plugin.getComponentLogger();
        final var config = YamlConfiguration.loadConfiguration(file);

        for (final var name : config.getKeys(false)) {
            final var location = config.getLocation(name);
            if (location != null) {
                warps.put(name, location);
            } else {
                logger.warn("Warp '{}' does not contain a valid location", name);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWarp(WarpEvent event) {
        final var name = event.getName();
        final var player = event.getPlayer();
        final var location = warps.get(name);

        if (location != null) {
            player.teleport(location);
        } else {
            player.sendMessage(Component.text("Warp not found", NamedTextColor.RED));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSetWarp(SetWarpEvent event) {
        final var name = event.getName();
        final var player = event.getPlayer();
        final var location = player.getLocation();

        if (warps.put(name, location) == null) {
            player.sendMessage("Warp created");
        } else {
            player.sendMessage("Warp set");
        }

        saveWarps();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDelWarp(DelWarpEvent event) {
        final var name = event.getName();
        final var sender = event.getSender();

        if (warps.remove(name) != null) {
            sender.sendMessage("Warp deleted");
            saveWarps();
        } else {
            sender.sendMessage(Component.text("Warp not found", NamedTextColor.RED));
        }
    }

    private void saveWarps() {
        final var snapshot = Map.copyOf(warps);
        CompletableFuture.runAsync(() -> {
            try {
                final var config = new YamlConfiguration();
                snapshot.forEach(config::set);
                config.save(file);
            } catch (IOException exception) {
                plugin.getComponentLogger().error("Could not save warps.yml", exception);
            }
        });
    }
}
