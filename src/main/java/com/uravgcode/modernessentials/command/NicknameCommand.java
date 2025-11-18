package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.event.NicknameEvent;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

@SuppressWarnings("unused")
public final class NicknameCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("nickname")
            .requires(permission("essentials.nickname"))
            .then(Commands.argument("nickname", StringArgumentType.word())
                .executes(this::nicknameSelf))
            .then(Commands.argument("target", ArgumentTypes.player())
                .requires(permission("essentials.nickname.others"))
                .then(Commands.argument("nickname", StringArgumentType.word())
                    .executes(this::nicknameOther)))
            .build();
    }

    private int nicknameSelf(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        final var nicknameArgument = context.getArgument("nickname", String.class);
        final var nickname = nicknameArgument.equals(player.getName()) ? null : nicknameArgument;

        player.getServer().getPluginManager().callEvent(new NicknameEvent(player, nickname));
        if (nickname == null) {
            player.sendMessage("Nickname removed");
        } else {
            player.sendMessage("Nickname changed");
        }

        return Command.SINGLE_SUCCESS;
    }

    private int nicknameOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
        final var target = targetResolver.resolve(context.getSource()).getFirst();
        final var nicknameArgument = context.getArgument("nickname", String.class);
        final var nickname = nicknameArgument.equals(target.getName()) ? null : nicknameArgument;

        target.getServer().getPluginManager().callEvent(new NicknameEvent(target, nickname));
        if (nickname == null) {
            context.getSource().getSender().sendMessage("Nickname removed");
        } else {
            context.getSource().getSender().sendMessage("Nickname changed");
        }

        return Command.SINGLE_SUCCESS;
    }
}
