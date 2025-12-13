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
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(false);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
        new UpdateChecker(this).checkForUpdate();
        configUpdater = new ConfigUpdater(this);
        moduleManager = new ModuleManager(this);
        reload();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        configUpdater.updateConfig();
        moduleManager.reloadModules();
    }
}
