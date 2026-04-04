package com.uravgcode.modernessentials;

import com.github.retrooper.packetevents.PacketEvents;
import com.uravgcode.modernessentials.manager.ModuleManager;
import com.uravgcode.modernessentials.update.ConfigUpdater;
import com.uravgcode.modernessentials.update.UpdateChecker;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class ModernEssentials extends JavaPlugin {
    private static ModernEssentials instance = null;

    private ConfigUpdater configUpdater = null;
    private ModuleManager moduleManager = null;

    public static @NotNull ModernEssentials instance() {
        return Objects.requireNonNull(instance, "plugin not initialized");
    }

    @Override
    public void onLoad() {
        ModernEssentials.instance = this;
        try {
            PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
            PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(false);
            PacketEvents.getAPI().load();
        } catch (Exception exception) {
            getComponentLogger().error("Failed to load PacketEvents API: {}", exception.getMessage());
        }
    }

    @Override
    public void onEnable() {
        try {
            PacketEvents.getAPI().init();
        } catch (Exception exception) {
            getComponentLogger().error("Failed to initialize PacketEvents API: {}", exception.getMessage());
        }

        new UpdateChecker(this).checkForUpdate();
        configUpdater = new ConfigUpdater(this);
        moduleManager = new ModuleManager(this);
        reload();
    }

    @Override
    public void onDisable() {
        try {
            PacketEvents.getAPI().terminate();
        } catch (Exception exception) {
            getComponentLogger().error("Failed to terminate PacketEvents API: {}", exception.getMessage());
        }
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        configUpdater.updateConfig();
        moduleManager.reloadModules();
    }
}
