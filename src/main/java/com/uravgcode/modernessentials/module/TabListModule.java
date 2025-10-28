package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigModule;
import com.uravgcode.modernessentials.annotation.ConfigValue;
import com.uravgcode.modernessentials.placeholder.Placeholders;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@ConfigModule(path = "tab-list")
public final class TabListModule extends PluginModule {
    private final MiniMessage miniMessage;

    @ConfigValue(path = "tab-list.format")
    private String format = "<player>";

    @ConfigValue(path = "tab-list.header")
    private String headerString = "";

    @ConfigValue(path = "tab-list.footer")
    private String footerString = "";

    @ConfigValue(path = "tab-list.refresh-interval")
    private long refreshInterval = 20L;

    public TabListModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        miniMessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders(),
            Placeholders.audiencePlaceholders()
        )).build();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        player.getScheduler().runAtFixedRate(plugin, task -> {
            var name = miniMessage.deserialize(format, player);
            var header = miniMessage.deserialize(headerString, player);
            var footer = miniMessage.deserialize(footerString, player);
            player.playerListName(name);
            player.sendPlayerListHeaderAndFooter(header, footer);
        }, null, 1L, refreshInterval);
    }
}
