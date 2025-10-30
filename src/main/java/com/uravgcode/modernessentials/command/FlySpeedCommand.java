package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class FlySpeedCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("flyspeed")
            .requires(permission("essentials.flyspeed"))
            .then(Commands.argument("speed", FloatArgumentType.floatArg(0.0f, 1.0f))
                .executes(this::setFlySpeed))
            .executes(this::setDefaultFlySpeed)
            .build();
    }

    private int setFlySpeed(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        final var speed = FloatArgumentType.getFloat(context, "speed");
        player.setFlySpeed(speed);
        player.sendMessage("fly speed set to " + speed);
        return Command.SINGLE_SUCCESS;
    }

    private int setDefaultFlySpeed(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        player.setFlySpeed(0.1f);
        player.sendMessage("fly speed reset to default");
        return Command.SINGLE_SUCCESS;
    }
}
