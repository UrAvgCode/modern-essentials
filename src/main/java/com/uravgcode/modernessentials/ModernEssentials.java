package com.uravgcode.modernessentials;

import com.uravgcode.modernessentials.listener.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModernEssentials extends JavaPlugin {
    private PingListener pingListener = null;
    private JoinListener joinListener = null;
    private ChatListener chatListener = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        pingListener = new PingListener(this);
        joinListener = new JoinListener(this);
        chatListener = new ChatListener(this);

        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(pingListener, this);
        pluginManager.registerEvents(joinListener, this);
        pluginManager.registerEvents(chatListener, this);
        pluginManager.registerEvents(new FlyListener(this), this);
        pluginManager.registerEvents(new GodListener(), this);
    }

    public void reload() {
        reloadConfig();
        pingListener.reload();
        joinListener.reload();
        chatListener.reload();
    }
}
