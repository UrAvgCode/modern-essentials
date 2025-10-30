package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

@SuppressWarnings("unused")
public final class InventorySeeCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("inventorysee")
            .requires(permission("essentials.inventorysee"))
            .then(Commands.argument("target", ArgumentTypes.player())
                .executes(this::openInventory))
            .build();
    }

    private int openInventory(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        final var targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
        final var target = targetResolver.resolve(context.getSource()).getFirst();
        player.openInventory(target.getInventory());
        return Command.SINGLE_SUCCESS;
    }
}
