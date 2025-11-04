package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.manager.ModuleManager;
import com.uravgcode.modernessentials.update.UpdateChecker;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        new UpdateChecker().checkForUpdate(this);
        final var pluginVersion = getPluginMeta().getVersion();
        final var configVersion = getConfig().getString("config.version", "0.0.0");
        if (!pluginVersion.equals(configVersion)) updateConfig();

        ModuleManager.initializeModules(this);
        reload();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        ModuleManager.reloadModules();
    }

    private void updateConfig() {
        final var logger = getComponentLogger();
        final var configPath = getDataPath().resolve("config.yml");
        final var backupPath = getDataPath().resolve("backups").resolve("config.yml");

        try {
            final var config = getConfig();
            if (!(config.getDefaults() instanceof final FileConfiguration updatedConfig)) return;

            for (final var key : config.getKeys(true)) {
                if (key.startsWith("config")) continue;

                final var value = config.get(key);
                if (value == null || value instanceof ConfigurationSection) continue;
                if (!updatedConfig.contains(key) || updatedConfig.isConfigurationSection(key)) continue;

                updatedConfig.set(key, value);
            }

            Files.createDirectories(backupPath.getParent());
            Files.move(configPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            updatedConfig.save(configPath.toFile());
            logger.info("successfully updated config.yml");
        } catch (IOException exception) {
            logger.warn("failed to update config.yml: {}", exception.getMessage());
        }
    }
}
