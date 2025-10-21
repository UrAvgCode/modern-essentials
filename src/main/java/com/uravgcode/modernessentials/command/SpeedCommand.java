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
                .executes(SpeedCommand::execute)
            ).build()
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getExecutor() instanceof Player player) {
            float speed = FloatArgumentType.getFloat(context, "speed");
            player.setWalkSpeed(speed);
        }
        return Command.SINGLE_SUCCESS;
    }
}
