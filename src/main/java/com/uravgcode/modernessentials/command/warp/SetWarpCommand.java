package com.uravgcode.modernessentials.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.argument.WarpArgument;
import com.uravgcode.modernessentials.command.CommandBuilder;
import com.uravgcode.modernessentials.event.warp.SetWarpEvent;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class SetWarpCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("setwarp")
            .requires(permission("essentials.setwarp"))
            .then(Commands.argument("name", new WarpArgument())
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        final var name = context.getArgument("name", String.class);
        player.getServer().getPluginManager().callEvent(new SetWarpEvent(player, name));
        return Command.SINGLE_SUCCESS;
    }
}
