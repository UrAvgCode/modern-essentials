package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.event.GodEvent;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class GodCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("god")
            .requires(permission("essentials.god"))
            .executes(this::execute)
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        player.getServer().getPluginManager().callEvent(new GodEvent(player));
        return Command.SINGLE_SUCCESS;
    }
}
