package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class SpeedCommand implements BaseCommand {

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("speed")
            .requires(playerPermission("essentials.speed"))
            .then(Commands.argument("speed", FloatArgumentType.floatArg(0.0f, 1.0f))
                .executes(SpeedCommand::setWalkSpeed))
            .executes(SpeedCommand::setDefaultWalkSpeed)
            .build()
        );
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
