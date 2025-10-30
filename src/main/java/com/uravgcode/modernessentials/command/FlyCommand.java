package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.event.FlyEvent;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.NamespacedKey;

@SuppressWarnings("unused")
public final class FlyCommand implements CommandBuilder {
    public static final NamespacedKey FLY_KEY = new NamespacedKey("modern-essentials", "fly");

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("fly")
            .requires(permission("essentials.fly"))
            .executes(this::execute)
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        final var player = player(context);
        player.getServer().getPluginManager().callEvent(new FlyEvent(player));
        return Command.SINGLE_SUCCESS;
    }
}
