package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigModule;
import com.uravgcode.modernessentials.annotation.ConfigValue;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PluginModule implements Listener {
    protected final JavaPlugin plugin;
    protected boolean enabled;

    protected PluginModule(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.enabled = false;
    }

    public void reload() {
        final var config = plugin.getConfig();
        final var logger = plugin.getComponentLogger();

        for (final var field : getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigValue.class)) continue;
            final var annotation = field.getAnnotation(ConfigValue.class);
            final var path = annotation.path();

            try {
                field.setAccessible(true);
                final var value = config.get(path, field.get(this));
                field.set(this, convert(value, field.getType()));
            } catch (Exception exception) {
                logger.warn("failed to inject config value for {}: {}", field.getName(), exception.getMessage());
            } finally {
                field.setAccessible(false);
            }
        }

        if (getClass().isAnnotationPresent(ConfigModule.class)) {
            final var annotation = getClass().getAnnotation(ConfigModule.class);
            final var path = annotation.path() + ".enabled";

            if (config.getBoolean(path, false)) {
                enable();
            } else {
                disable();
            }
        } else {
            enable();
        }
    }

    public void enable() {
        if (!enabled) {
            enabled = true;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    public void disable() {
        if (enabled) {
            enabled = false;
            HandlerList.unregisterAll(this);
        }
    }

    private static @NotNull Object convert(@NotNull Object value, @NotNull Class<?> type) {
        return switch (value) {
            case List<?> list when type == String.class -> list.stream().map(Object::toString).collect(Collectors.joining("\n"));
            case String string when type == List.class -> List.of(string);
            default -> value;
        };
    }
}
