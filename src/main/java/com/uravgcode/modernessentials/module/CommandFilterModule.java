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

import java.util.Set;

@ConfigModule(path = "command-filter")
public final class CommandFilterModule extends PluginModule {
    private enum Mode {
        WHITELIST,
        BLACKLIST
    }

    @ConfigValue(path = "command-filter.mode")
    private Mode mode = Mode.BLACKLIST;

    @ConfigValue(path = "command-filter.commands")
    private Set<String> commands = Set.of();

    public CommandFilterModule(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        if (event.getPlayer().hasPermission("essentials.commandfilter.bypass")) return;
        event.getCommands().removeIf(command -> (mode == Mode.WHITELIST) != commands.contains(command));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final var player = event.getPlayer();
        if (player.hasPermission("essentials.commandfilter.bypass")) return;

        final var message = event.getMessage();
        final var command = message.substring(1);
        final var literal = command.split(" ")[0];
        if ((mode == Mode.WHITELIST) == commands.contains(literal)) return;

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
