package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.List;

@SuppressWarnings("unused")
public final class KickAllCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("kickall")
            .requires(permission("essentials.kickall"))
            .executes(this::execute)
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var players = List.copyOf(sender.getServer().getOnlinePlayers());

        for (final var player : players) {
            if (!player.equals(sender)) {
                player.kick();
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
