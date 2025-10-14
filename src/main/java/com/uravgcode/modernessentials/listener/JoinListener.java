package com.uravgcode.modernessentials.listener;

import com.uravgcode.modernessentials.placeholder.Placeholders;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class JoinListener implements Listener {
    private final JavaPlugin plugin;

    private MiniMessage minimessage = MiniMessage.miniMessage();
    private String headerString = "";
    private String footerString = "";
    private long refreshInterval = 20L;

    public JoinListener(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        player.getScheduler().runAtFixedRate(plugin, task -> {
            var header = minimessage.deserialize(headerString, player);
            var footer = minimessage.deserialize(footerString, player);
            player.sendPlayerListHeaderAndFooter(header, footer);
        }, null, 1L, refreshInterval);
    }

    public void reload() {
        final var config = plugin.getConfig().getConfigurationSection("tab-list");
        if (config == null) return;

        minimessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders(),
            Placeholders.audiencePlaceholders()
        )).build();

        headerString = String.join("\n", config.getStringList("header"));
        footerString = String.join("\n", config.getStringList("footer"));
        refreshInterval = config.getLong("refresh-interval", 20L);
    }
}
