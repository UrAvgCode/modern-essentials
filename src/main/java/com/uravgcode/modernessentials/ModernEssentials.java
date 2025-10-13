package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.listener.FlyListener;
import com.uravgcode.modernessentials.listener.GodListener;
import com.uravgcode.modernessentials.listener.PingListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModernEssentials extends JavaPlugin {

    private PingListener pingListener = null;

    @Override
    public void onEnable() {
        saveConfig();

        pingListener = new PingListener(this);

        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(pingListener, this);
        pluginManager.registerEvents(new FlyListener(this), this);
        pluginManager.registerEvents(new GodListener(), this);
    }

    public void reload() {
        reloadConfig();
        pingListener.reload();
    }
}
