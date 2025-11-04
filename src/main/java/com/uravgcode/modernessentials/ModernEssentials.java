package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.manager.ModuleManager;
import com.uravgcode.modernessentials.update.ConfigUpdater;
import com.uravgcode.modernessentials.update.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ModernEssentials extends JavaPlugin {
    private static ModernEssentials instance = null;

    private ConfigUpdater configUpdater = null;
    private ModuleManager moduleManager = null;

    public static @NotNull ModernEssentials instance() {
        return Objects.requireNonNull(instance, "plugin not initialized");
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        new UpdateChecker().checkForUpdate(this);
        configUpdater = new ConfigUpdater(this);
        moduleManager = new ModuleManager(this);
        reload();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        configUpdater.updateConfig();
        moduleManager.reloadModules();
    }
}
