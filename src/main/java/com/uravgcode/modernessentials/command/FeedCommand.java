package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.exception.NoPlayerFoundException;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public final class FeedCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("feed")
            .requires(permission("essentials.feed"))
            .then(Commands.argument("targets", ArgumentTypes.players())
                .requires(permission("essentials.feed.others"))
                .executes(this::feedOthers))
            .executes(this::feed)
            .build();
    }

    private int feed(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        player.setFoodLevel(20);
        player.setSaturation(10.0f);
        player.setExhaustion(0.0f);
        player.sendMessage(Component.text("Fed ").append(player.name()));
        return Command.SINGLE_SUCCESS;
    }

    private int feedOthers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var targetResolver = context.getArgument("targets", PlayerSelectorArgumentResolver.class);
        final var targets = targetResolver.resolve(context.getSource());
        if (targets.isEmpty()) throw new NoPlayerFoundException();

        for (final var target : targets) {
            target.setFoodLevel(20);
            target.setSaturation(10.0f);
            target.setExhaustion(0.0f);
        }

        final var sender = context.getSource().getSender();
        if (targets.size() == 1) {
            sender.sendMessage(Component.text("Fed ").append(targets.getFirst().name()));
        } else {
            sender.sendMessage(Component.text("Fed " + targets.size() + " players"));
        }

        return Command.SINGLE_SUCCESS;
    }
}
