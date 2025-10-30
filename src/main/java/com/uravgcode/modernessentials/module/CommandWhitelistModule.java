package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigModule;
import com.uravgcode.modernessentials.annotation.ConfigValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

@ConfigModule(path = "command-whitelist")
public final class CommandWhitelistModule extends PluginModule {
    private static final String permission = "essentials.commandwhitelist.bypass";

    @ConfigValue(path = "command-whitelist.whitelist")
    private Set<String> whitelist = Collections.emptySet();

    public CommandWhitelistModule(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        if (event.getPlayer().hasPermission(permission)) return;
        event.getCommands().removeIf(command -> !whitelist.contains(command));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final var player = event.getPlayer();
        if (player.hasPermission(permission)) return;

        final var message = event.getMessage();
        final var command = message.substring(1);
        final var literal = command.split(" ")[0];
        if (whitelist.contains(literal)) return;

        event.setCancelled(true);
        player.sendMessage(Component.textOfChildren(
            Component.translatable("command.unknown.command"),
            Component.newline(),
            Component.textOfChildren(
                Component.text(command),
                Component.translatable("command.context.here")
            ).clickEvent(ClickEvent.suggestCommand(message))
        ).color(NamedTextColor.RED));
    }
}
