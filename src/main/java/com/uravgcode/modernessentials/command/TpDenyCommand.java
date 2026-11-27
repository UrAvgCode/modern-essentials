package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.teleport.TeleportRequests;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// same as tpaccept but for saying no
@NullMarked
@SuppressWarnings("unused")
public final class TpDenyCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("tpdeny")
            .requires(source -> source.getSender().hasPermission("essentials.tpa.accept")
                || source.getSender().hasPermission("essentials.tpahere.accept"))
            .executes(context -> execute(context, null))
            .then(Commands.argument("player", ArgumentTypes.player())
                .executes(context -> execute(context, context.getArgument("player", PlayerSelectorArgumentResolver.class)
                    .resolve(context.getSource()).getFirst())))
            .build();
    }

    // no player given = deny the newest request
    private int execute(CommandContext<CommandSourceStack> context, @Nullable Player sender) throws CommandSyntaxException {
        final var player = player(context);
        if (!TeleportRequests.deny(player, sender)) {
            player.sendMessage(Component.text("You don't have any pending teleport requests", NamedTextColor.RED));
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }
}
