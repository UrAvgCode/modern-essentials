package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.argument.TeleportRequestPlayerArgument;
import com.uravgcode.modernessentials.event.teleport.TeleportRequestEvent;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public final class TpaHereCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("tpahere")
            .requires(permission("essentials.tpahere"))
            .then(Commands.argument("target", new TeleportRequestPlayerArgument("essentials.tpahere.accept"))
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        final var target = context.getArgument("target", Player.class);
        player.getServer().getPluginManager().callEvent(new TeleportRequestEvent(player, target, TeleportRequestEvent.Type.TPA_HERE));
        return Command.SINGLE_SUCCESS;
    }
}
