package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.event.FullBrightEvent;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class FullBrightCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("fullbright")
            .requires(permission("essentials.fullbright"))
            .executes(this::execute)
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        player.getServer().getPluginManager().callEvent(new FullBrightEvent(player));
        return Command.SINGLE_SUCCESS;
    }
}
