package com.uravgcode.modernessentials.command.home;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.argument.HomeArgument;
import com.uravgcode.modernessentials.command.CommandBuilder;
import com.uravgcode.modernessentials.event.home.HomeEvent;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public final class HomeCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("home")
            .requires(permission("essentials.home"))
            .then(Commands.argument("name", new HomeArgument())
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        final var name = context.getArgument("name", String.class);
        player.getServer().getPluginManager().callEvent(new HomeEvent(player, name));
        return Command.SINGLE_SUCCESS;
    }
}
