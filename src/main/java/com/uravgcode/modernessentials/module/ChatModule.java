package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigValue;
import com.uravgcode.modernessentials.placeholder.Placeholders;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class ChatModule extends PluginModule {
    private final MiniMessage miniMessage;

    @ConfigValue(name = "chat.format")
    private String format = "<gray><player> <dark_gray>> <white><message>";

    public ChatModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        miniMessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders(),
            Placeholders.audiencePlaceholders()
        )).build();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        event.renderer((player, playerName, message, viewer) ->
            miniMessage.deserialize(format, player, Placeholder.component("message", message))
        );
    }
}
