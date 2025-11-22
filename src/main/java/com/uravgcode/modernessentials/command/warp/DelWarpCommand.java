package com.uravgcode.modernessentials.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.argument.WarpArgument;
import com.uravgcode.modernessentials.command.CommandBuilder;
import com.uravgcode.modernessentials.event.warp.DelWarpEvent;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class DelWarpCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("delwarp")
            .requires(permission("essentials.delwarp"))
            .then(Commands.argument("name", new WarpArgument())
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var name = context.getArgument("name", String.class);
        sender.getServer().getPluginManager().callEvent(new DelWarpEvent(sender, name));
        return Command.SINGLE_SUCCESS;
    }
}
