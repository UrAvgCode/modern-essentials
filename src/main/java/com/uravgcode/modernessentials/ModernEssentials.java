package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.registry.ModuleRegistrar;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModernEssentials extends JavaPlugin {

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
