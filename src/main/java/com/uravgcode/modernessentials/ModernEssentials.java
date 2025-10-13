package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.listener.FlyListener;
import com.uravgcode.modernessentials.listener.GodListener;
import com.uravgcode.modernessentials.listener.PingListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModernEssentials extends JavaPlugin {

    @Override
    public void onEnable() {
        saveConfig();

        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PingListener(this), this);
        pluginManager.registerEvents(new FlyListener(this), this);
        pluginManager.registerEvents(new GodListener(), this);
    }
}
