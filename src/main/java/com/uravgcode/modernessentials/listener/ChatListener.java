package com.uravgcode.modernessentials.listener;

import com.uravgcode.modernessentials.placeholder.Placeholders;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ChatListener implements Listener {
    private final JavaPlugin plugin;
    private final MiniMessage miniMessage;

    private String format = "";

    public ChatListener(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        miniMessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders(),
            Placeholders.audiencePlaceholders()
        )).build();
        reload();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        event.renderer((player, playerName, message, viewer) ->
            miniMessage.deserialize(format, player, Placeholder.component("message", message))
        );
    }

    public void reload() {
        final var config = plugin.getConfig().getConfigurationSection("chat");
        if (config == null) return;

        format = config.getString("format", "");
    }
}
