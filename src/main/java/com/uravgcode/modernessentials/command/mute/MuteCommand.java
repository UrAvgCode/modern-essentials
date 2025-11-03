package com.uravgcode.modernessentials.command.mute;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.command.CommandBuilder;
import com.uravgcode.modernessentials.event.mute.MuteEvent;
import com.uravgcode.modernessentials.exception.NoPlayerFoundException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public final class MuteCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("mute")
            .requires(permission("essentials.mute"))
            .then(Commands.argument("targets", ArgumentTypes.players())
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var targetResolver = context.getArgument("targets", PlayerSelectorArgumentResolver.class);
        final var targets = targetResolver.resolve(context.getSource());
        if (targets.isEmpty()) throw new NoPlayerFoundException();

        final var sender = context.getSource().getSender();
        final var pluginManager = sender.getServer().getPluginManager();
        for (final var target : targets) {
            pluginManager.callEvent(new MuteEvent(target));
        }

        if (targets.size() == 1) {
            sender.sendMessage(Component.text("Muted ").append(targets.getFirst().name()));
        } else {
            sender.sendMessage(Component.text("Muted " + targets.size() + " players"));
        }

        return Command.SINGLE_SUCCESS;
    }
}
