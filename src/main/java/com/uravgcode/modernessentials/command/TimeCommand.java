package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.uravgcode.modernessentials.ModernEssentials;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public final class TimeCommand implements BaseCommand {

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
                .requires(playerPermission("essentials.time"))
                .executes(context -> setTime(context, time))
                .build()
            )
        );
    }

    private static int setTime(CommandContext<CommandSourceStack> context, long time) {
        final var executor = Objects.requireNonNull(context.getSource().getExecutor());
        final var plugin = ModernEssentials.instance();
        final var world = executor.getWorld();

        executor.sendMessage(Component.translatable("commands.time.set", Component.text(time)));
        executor.getServer().getGlobalRegionScheduler().execute(plugin, () -> world.setTime(time));

        return Command.SINGLE_SUCCESS;
    }
}
