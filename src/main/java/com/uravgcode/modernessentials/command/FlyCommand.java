package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.event.FlyEvent;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class FlyCommand implements PluginCommand {
    public static final NamespacedKey FLY_KEY = new NamespacedKey("modern-essentials", "fly");

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("fly")
            .requires(playerPermission("essentials.fly"))
            .executes(FlyCommand::execute)
            .build();
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getSender() instanceof Player player) {
            player.getServer().getPluginManager().callEvent(new FlyEvent(player));
        }
        return Command.SINGLE_SUCCESS;
    }
}
