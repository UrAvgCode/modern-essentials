package com.uravgcode.modernessentials.module;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.uravgcode.modernessentials.annotation.ConfigValue;
import com.uravgcode.modernessentials.placeholder.Placeholders;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ServerListModule extends PluginModule {
    private final MiniMessage miniMessage;

    @ConfigValue(name = "server-list.motd")
    private List<String> motds = Collections.emptyList();

    @ConfigValue(name = "server-list.max-players")
    private int maxPlayers = -1;

    @ConfigValue(name = "server-list.fake-players")
    private int fakePlayers = -1;

    @ConfigValue(name = "server-list.hide-player-count")
    private boolean hidePlayerCount = false;

    @ConfigValue(name = "server-list.disable-player-list-hover")
    private boolean disablePlayerListHover = false;

    public ServerListModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        this.miniMessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders()
        )).build();
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
}
