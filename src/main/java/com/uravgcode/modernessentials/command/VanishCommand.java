package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.event.VanishEvent;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class VanishCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("vanish")
            .requires(permission("essentials.vanish"))
            .executes(this::execute)
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        player.getServer().getPluginManager().callEvent(new VanishEvent(player));
        return Command.SINGLE_SUCCESS;
    }
}
