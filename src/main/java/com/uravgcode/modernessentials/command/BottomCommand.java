package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("unused")
public final class BottomCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("bottom")
            .requires(permission("essentials.bottom"))
            .executes(this::execute)
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        final var location = player.getLocation();
        final var world = location.getWorld();

        final var x = location.getBlockX();
        final var z = location.getBlockZ();
        for (var y = world.getMinHeight(); y <= world.getLogicalHeight() - 2; ++y) {
            final var ground = world.getBlockAt(x, y, z);
            if (!ground.isSolid()) continue;

            final var standing = ground.getRelative(0, 1, 0);
            if (!standing.isPassable() || standing.isLiquid()) continue;

            final var head = standing.getRelative(0, 1, 0);
            if (!head.isPassable() || head.isLiquid()) continue;

            location.setY(y + 1);
            break;
        }

        player.teleport(location);
        return Command.SINGLE_SUCCESS;
    }
}
