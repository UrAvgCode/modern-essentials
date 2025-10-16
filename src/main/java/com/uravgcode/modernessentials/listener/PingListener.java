package com.uravgcode.modernessentials.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.uravgcode.modernessentials.placeholder.Placeholders;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PingListener implements Listener {
    private final JavaPlugin plugin;
    private final MiniMessage miniMessage;

    private List<String> motds = Collections.emptyList();
    private int maxPlayers = -1;
    private int fakePlayers = -1;
    private boolean hidePlayerCount = false;
    private boolean disablePlayerListHover = false;

    public PingListener(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders()
        )).build();
        reload();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onServerListPing(PaperServerListPingEvent event) {
        if (!motds.isEmpty()) {
            var motd = motds.size() == 1
                ? miniMessage.deserialize(motds.getFirst())
                : miniMessage.deserialize(motds.get(ThreadLocalRandom.current().nextInt(motds.size())));
            event.motd(motd);
        }

        if (maxPlayers >= 0) event.setMaxPlayers(maxPlayers);
        if (fakePlayers >= 0) event.setNumPlayers(fakePlayers);
        if (hidePlayerCount) event.setHidePlayers(true);
        if (disablePlayerListHover) event.getListedPlayers().clear();
    }

    public void reload() {
        final var config = plugin.getConfig().getConfigurationSection("server-list");
        if (config == null) return;

        motds = config.getStringList("motd");
        maxPlayers = config.getInt("max-players", -1);
        fakePlayers = config.getInt("fake-players", -1);
        hidePlayerCount = config.getBoolean("hide-player-count", false);
        disablePlayerListHover = config.getBoolean("disable-player-list-hover", false);
    }
}
