package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigValue;
import com.uravgcode.modernessentials.placeholder.Placeholders;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class JoinQuitModule extends PluginModule {
    private final MiniMessage miniMessage;

    @ConfigValue(name = "join-quit.join-message")
    private String joinMessage = "<green>+ <player> <gray>Connected";

    @ConfigValue(name = "join-quit.quit-message")
    private String quitMessage = "<red>- <player> <gray>Disconnected";

    public JoinQuitModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        miniMessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders(),
            Placeholders.audiencePlaceholders()
        )).build();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(miniMessage.deserialize(joinMessage, event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(miniMessage.deserialize(quitMessage, event.getPlayer()));
    }
}
