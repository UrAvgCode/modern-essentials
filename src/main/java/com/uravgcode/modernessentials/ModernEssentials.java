package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.registry.ModuleRegistrar;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ModernEssentials extends JavaPlugin {
    private static ModernEssentials instance = null;

    public static @NotNull ModernEssentials instance() {
        return Objects.requireNonNull(instance, "plugin not initialized");
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ModuleRegistrar.registerAll(this);
        ModuleRegistrar.reloadAll();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        ModuleRegistrar.reloadAll();
    }
}
