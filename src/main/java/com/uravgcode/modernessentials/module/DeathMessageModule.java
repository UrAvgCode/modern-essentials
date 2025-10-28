package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigModule;
import com.uravgcode.modernessentials.annotation.ConfigValue;
import com.uravgcode.modernessentials.placeholder.Placeholders;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@ConfigModule(path = "death-message")
public final class DeathMessageModule extends PluginModule {
    private final MiniMessage miniMessage;

    @ConfigValue(path = "death-message.format")
    private String format = "<message>";

    public DeathMessageModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        miniMessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders(),
            Placeholders.audiencePlaceholders()
        )).build();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final var message = event.deathMessage();
        if (message == null) return;

        final var player = event.getPlayer();
        event.deathMessage(miniMessage.deserialize(format, player, Placeholder.component("message", message)));
    }
}
