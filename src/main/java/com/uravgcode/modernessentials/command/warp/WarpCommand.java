package com.uravgcode.modernessentials.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.command.CommandBuilder;
import com.uravgcode.modernessentials.event.warp.WarpEvent;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import com.uravgcode.modernessentials.module.WarpModule;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class WarpCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("warp")
            .requires(permission("essentials.warp"))
            .then(Commands.argument("name", StringArgumentType.word())
                .suggests(WarpModule::suggestions)
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        final var name = StringArgumentType.getString(context, "name");
        player.getServer().getPluginManager().callEvent(new WarpEvent(player, name));
        return Command.SINGLE_SUCCESS;
    }
}
