package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.event.GodEvent;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class GodCommand implements PluginCommand {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("god")
            .requires(playerPermission("essentials.god"))
            .executes(GodCommand::execute)
            .build();
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getSender() instanceof Player player) {
            player.getServer().getPluginManager().callEvent(new GodEvent(player));
        }
        return Command.SINGLE_SUCCESS;
    }
}
