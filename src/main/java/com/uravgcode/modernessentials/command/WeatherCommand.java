package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;

import java.util.Map;

public class WeatherCommand {

    public static void registerCommands(Commands registrar) {
        var weathers = Map.<String, Command<CommandSourceStack>>of(
            "sun", ctx -> setWeather(ctx, false, false, "commands.weather.set.clear"),
            "rain", ctx -> setWeather(ctx, true, false, "commands.weather.set.rain"),
            "thunder", ctx -> setWeather(ctx, true, true, "commands.weather.set.thunder")
        );

        weathers.forEach((name, executor) ->
            registrar.register(Commands.literal(name)
                .requires(sender -> sender.getSender().hasPermission("essentials.weather"))
                .executes(executor)
                .build()
            )
        );
    }

    private static int setWeather(CommandContext<CommandSourceStack> context, boolean storm, boolean thunder, String key) {
        var executor = context.getSource().getExecutor();
        if (executor != null) {
            var world = executor.getWorld();
            world.setStorm(storm);
            world.setThundering(thunder);
            executor.sendMessage(Component.translatable(key));
        }
        return Command.SINGLE_SUCCESS;
    }
}
