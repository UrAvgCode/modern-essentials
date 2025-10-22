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
public final class WeatherCommand implements BaseCommand {

    @Override
    public void register(@NotNull Commands registrar) {
        final var weathers = Map.<String, Command<CommandSourceStack>>of(
            "sun", context -> setWeather(context, false, false, "commands.weather.set.clear"),
            "rain", context -> setWeather(context, true, false, "commands.weather.set.rain"),
            "thunder", context -> setWeather(context, true, true, "commands.weather.set.thunder")
        );

        weathers.forEach((name, executor) ->
            registrar.register(Commands.literal(name)
                .requires(permission("essentials.weather"))
                .executes(executor)
                .build()
            )
        );
    }

    private static int setWeather(CommandContext<CommandSourceStack> context, boolean storm, boolean thunder, @NotNull String key) {
        final var plugin = ModernEssentials.instance();
        final var sender = context.getSource().getSender();
        final var server = sender.getServer();

        final var executor = context.getSource().getExecutor();
        final var world = executor != null ? executor.getWorld() : server.getRespawnWorld();

        sender.sendMessage(Component.translatable(key));
        server.getGlobalRegionScheduler().execute(plugin, () -> {
            world.setStorm(storm);
            world.setThundering(thunder);
        });

        return Command.SINGLE_SUCCESS;
    }
}
