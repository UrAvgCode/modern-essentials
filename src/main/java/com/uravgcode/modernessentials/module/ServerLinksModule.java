package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigModule;
import com.uravgcode.modernessentials.annotation.ConfigValue;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.ServerLinks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLinksSendEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ConfigModule(path = "server-links")
public final class ServerLinksModule extends PluginModule {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @ConfigValue(path = "server-links.links")
    private List<Map<?, ?>> links = Collections.emptyList();

    public ServerLinksModule(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLinksSend(PlayerLinksSendEvent event) {
        final var serverLinks = event.getLinks();

        if (event.getConnection() instanceof PlayerConfigurationConnection connection) {
            final var profile = connection.getProfile();
            final var name = profile.getName();
            final var uuid = profile.getId();

            for (final var entry : links) {
                final var uri = URI.create(String.valueOf(entry.get("url"))
                    .replace("<player>", String.valueOf(name))
                    .replace("<uuid>", String.valueOf(uuid)));

                if (entry.get("type") != null) {
                    final var type = ServerLinks.Type.valueOf(String.valueOf(entry.get("type")).toUpperCase());
                    serverLinks.addLink(type, uri);
                } else if (entry.get("name") != null) {
                    serverLinks.addLink(miniMessage.deserialize(
                        String.valueOf(entry.get("name")),
                        Placeholder.unparsed("player", String.valueOf(name)),
                        Placeholder.unparsed("uuid", String.valueOf(uuid))
                    ), uri);
                }
            }
        }
    }
}
