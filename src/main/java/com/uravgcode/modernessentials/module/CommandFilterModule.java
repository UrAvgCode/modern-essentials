package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigModule;
import com.uravgcode.modernessentials.annotation.ConfigValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
        event.getCommands().removeIf(command -> (mode == Mode.WHITELIST) != commands.contains(getLiteral(command)));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final var player = event.getPlayer();
        if (player.hasPermission("essentials.commandfilter.bypass")) return;

        final var message = event.getMessage();
        final var literal = getLiteral(message);
        if ((mode == Mode.WHITELIST) == commands.contains(literal)) return;

        event.setCancelled(true);
        player.sendMessage(Component.textOfChildren(
            Component.translatable("command.unknown.command"),
            Component.newline(),
            Component.textOfChildren(
                Component.text(message.substring(1))
                    .decorate(TextDecoration.UNDERLINED),
                Component.translatable("command.context.here")
            ).clickEvent(ClickEvent.suggestCommand(message))
        ).color(NamedTextColor.RED));
    }

    private @NotNull String getLiteral(@NotNull String command) {
        final var spaceIndex = command.indexOf(' ');
        final var end = spaceIndex != -1 ? spaceIndex : command.length();
        final var colonIndex = command.indexOf(':');
        final var start = colonIndex != -1 && colonIndex < end ? colonIndex + 1 : (command.startsWith("/") ? 1 : 0);
        return command.substring(start, end);
    }
}
