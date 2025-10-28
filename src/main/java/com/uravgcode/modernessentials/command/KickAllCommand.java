package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class KickAllCommand implements PluginCommand {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("kickall")
            .requires(permission("essentials.kickall"))
            .executes(KickAllCommand::execute)
            .build();
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var executor = source.getExecutor();
        final var server = source.getSender().getServer();

        for (var player : server.getOnlinePlayers()) {
            if (!player.equals(executor)) {
                player.kick();
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
