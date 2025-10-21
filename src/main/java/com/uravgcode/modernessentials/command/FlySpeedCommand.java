package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class FlySpeedCommand implements BaseCommand {

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("flyspeed")
            .requires(sender -> sender.getSender().hasPermission("essentials.flyspeed"))
            .then(Commands.argument("flyspeed", FloatArgumentType.floatArg(0.0f, 1.0f))
                .executes(FlySpeedCommand::execute)
            ).build()
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getExecutor() instanceof Player player) {
            float flySpeed = FloatArgumentType.getFloat(context, "flyspeed");
            player.setFlySpeed(flySpeed);
        }
        return Command.SINGLE_SUCCESS;
    }
}
