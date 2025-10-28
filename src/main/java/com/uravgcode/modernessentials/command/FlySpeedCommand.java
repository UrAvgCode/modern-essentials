package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class FlySpeedCommand implements PluginCommand {

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("flyspeed")
            .requires(playerPermission("essentials.flyspeed"))
            .then(Commands.argument("speed", FloatArgumentType.floatArg(0.0f, 1.0f))
                .executes(FlySpeedCommand::setFlySpeed))
            .executes(FlySpeedCommand::setDefaultFlySpeed)
            .build()
        );
    }

    private static int setFlySpeed(CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var speed = FloatArgumentType.getFloat(context, "speed");
        player.setFlySpeed(speed);
        player.sendMessage("fly speed set to " + speed);
        return Command.SINGLE_SUCCESS;
    }

    private static int setDefaultFlySpeed(CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        player.setFlySpeed(0.1f);
        player.sendMessage("fly speed reset to default");
        return Command.SINGLE_SUCCESS;
    }
}
