package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class SpeedCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("speed")
            .requires(permission("essentials.speed"))
            .then(Commands.argument("speed", FloatArgumentType.floatArg(0.0f, 1.0f))
                .executes(this::setWalkSpeed))
            .executes(this::setDefaultWalkSpeed)
            .build();
    }

    private int setWalkSpeed(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        final var speed = FloatArgumentType.getFloat(context, "speed");
        player.setWalkSpeed(speed);
        player.sendMessage("walk speed set to " + speed);
        return Command.SINGLE_SUCCESS;
    }

    private int setDefaultWalkSpeed(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        player.setWalkSpeed(0.2f);
        player.sendMessage("walk speed reset to default");
        return Command.SINGLE_SUCCESS;
    }
}
