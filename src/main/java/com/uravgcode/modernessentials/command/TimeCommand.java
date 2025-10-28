package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.uravgcode.modernessentials.ModernEssentials;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SuppressWarnings("unused")
public final class TimeCommand implements PluginCommand {

    @Override
    public void register(@NotNull Commands registrar) {
        final var times = Map.of(
            "day", 1000L,
            "night", 13000L,
            "noon", 6000L,
            "midnight", 18000L,
            "sunrise", 23000L,
            "sunset", 12000L
        );

        times.forEach((name, time) ->
            registrar.register(Commands.literal(name)
                .requires(permission("essentials.time"))
                .executes(context -> setTime(context, time))
                .build()
            )
        );
    }

    private static int setTime(CommandContext<CommandSourceStack> context, long time) {
        final var plugin = ModernEssentials.instance();
        final var sender = context.getSource().getSender();
        final var server = sender.getServer();

        final var executor = context.getSource().getExecutor();
        final var world = executor != null ? executor.getWorld() : server.getRespawnWorld();

        sender.sendMessage(Component.translatable("commands.time.set", Component.text(time)));
        server.getGlobalRegionScheduler().execute(plugin, () -> world.setTime(time));

        return Command.SINGLE_SUCCESS;
    }
}
