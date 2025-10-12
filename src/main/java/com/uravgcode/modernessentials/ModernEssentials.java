package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.listener.FlyListener;
import com.uravgcode.modernessentials.listener.GodListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModernEssentials extends JavaPlugin {

    @Override
    public void onEnable() {
        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new GodListener(), this);
        pluginManager.registerEvents(new FlyListener(this), this);
    }
}
