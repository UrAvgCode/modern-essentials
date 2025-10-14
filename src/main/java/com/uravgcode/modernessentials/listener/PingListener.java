package com.uravgcode.modernessentials.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.uravgcode.modernessentials.placeholder.Placeholders;
import net.kyori.adventure.text.Component;
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

    private List<Component> motds = Collections.emptyList();
    private int maxPlayers = -1;
    private int fakePlayers = -1;
    private boolean hidePlayerCount = false;
    private boolean disablePlayerListHover = false;

    public PingListener(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onServerListPing(PaperServerListPingEvent event) {
        if (!motds.isEmpty()) {
            event.motd(motds.size() == 1 ? motds.getFirst() : motds.get(ThreadLocalRandom.current().nextInt(motds.size())));
        }

        if (maxPlayers >= 0) event.setMaxPlayers(maxPlayers);
        if (fakePlayers >= 0) event.setNumPlayers(fakePlayers);
        if (hidePlayerCount) event.setHidePlayers(true);
        if (disablePlayerListHover) event.getListedPlayers().clear();
    }

    public void reload() {
        final var config = plugin.getConfig().getConfigurationSection("server-list");
        if (config == null) return;

        var minimessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders()
        )).build();

        final var motdStrings = config.getStringList("motd");
        motds = motdStrings.stream().map(minimessage::deserialize).toList();

        maxPlayers = config.getInt("max-players", -1);
        fakePlayers = config.getInt("fake-players", -1);
        hidePlayerCount = config.getBoolean("hide-player-count", false);
        disablePlayerListHover = config.getBoolean("disable-player-list-hover", false);
    }
}
