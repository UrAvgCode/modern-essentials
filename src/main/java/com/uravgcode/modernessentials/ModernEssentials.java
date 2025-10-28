package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.manager.ModuleManager;
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
        ModuleManager.initializeModules(this);
        reload();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        ModuleManager.reloadModules();
    }
}
