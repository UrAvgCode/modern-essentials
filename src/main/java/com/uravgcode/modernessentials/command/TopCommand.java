package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@SuppressWarnings("unused")
public final class TopCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("top")
            .requires(permission("essentials.top"))
            .executes(this::execute)
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        final var location = player.getLocation();
        final var top = location.getWorld().getHighestBlockYAt(location);

        location.setY(top + 1);
        player.teleportAsync(location, TeleportCause.COMMAND);
        return Command.SINGLE_SUCCESS;
    }
}
