package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class SpeedCommand implements PluginCommand {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("speed")
            .requires(playerPermission("essentials.speed"))
            .then(Commands.argument("speed", FloatArgumentType.floatArg(0.0f, 1.0f))
                .executes(SpeedCommand::setWalkSpeed))
            .executes(SpeedCommand::setDefaultWalkSpeed)
            .build();
    }

    private static int setWalkSpeed(CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var speed = FloatArgumentType.getFloat(context, "speed");
        player.setWalkSpeed(speed);
        player.sendMessage("walk speed set to " + speed);
        return Command.SINGLE_SUCCESS;
    }

    private static int setDefaultWalkSpeed(CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        player.setWalkSpeed(0.2f);
        player.sendMessage("walk speed reset to default");
        return Command.SINGLE_SUCCESS;
    }
}
