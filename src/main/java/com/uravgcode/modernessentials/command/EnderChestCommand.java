package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

@SuppressWarnings("unused")
public final class EnderChestCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("enderchest")
            .requires(permission("essentials.enderchest"))
            .then(Commands.argument("target", ArgumentTypes.player())
                .requires(permission("essentials.enderchest.others"))
                .executes(this::openOtherEnderChest))
            .executes(this::openEnderChest)
            .build();
    }

    private int openEnderChest(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        player.openInventory(player.getEnderChest());
        return Command.SINGLE_SUCCESS;
    }

    private int openOtherEnderChest(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        final var targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
        final var target = targetResolver.resolve(context.getSource()).getFirst();
        player.openInventory(target.getEnderChest());
        return Command.SINGLE_SUCCESS;
    }
}
