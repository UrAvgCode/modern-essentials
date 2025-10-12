package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.listener.GodListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModernEssentials extends JavaPlugin {

    @Override
    public void onEnable() {
        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new GodListener(), this);
    }
}
