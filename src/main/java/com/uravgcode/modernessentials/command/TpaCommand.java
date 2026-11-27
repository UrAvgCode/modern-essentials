package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.argument.TeleportRequestPlayerArgument;
import com.uravgcode.modernessentials.teleport.TeleportRequests;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class TpaCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("tpa")
            .requires(permission("essentials.tpa"))
            .then(Commands.argument("target", new TeleportRequestPlayerArgument("essentials.tpa.accept"))
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        final var target = context.getArgument("target", Player.class);

        TeleportRequests.add(player, target, TeleportRequests.Type.TPA);

        final var options = ClickCallback.Options.builder()
            .lifetime(TeleportRequests.LIFETIME)
            .uses(1)
            .build();

        final var message = Component.textOfChildren(
            player.name(),
            Component.text(" has requested to teleport to you "),
            Component.text("[Accept]", NamedTextColor.GREEN)
                .clickEvent(ClickEvent.callback(_ -> {
                    if (!TeleportRequests.accept(target, player)) {
                        target.sendMessage(Component.text("This request already expired", NamedTextColor.RED));
                    }
                }, options))
                .hoverEvent(Component.text("Click to accept")),
            Component.text(" "),
            Component.text("[Deny]", NamedTextColor.RED)
                .clickEvent(ClickEvent.callback(_ -> TeleportRequests.deny(target, player), options))
                .hoverEvent(Component.text("Click to deny")),
            Component.newline(),
            Component.text("You can also type /tpaccept or /tpdeny", NamedTextColor.GRAY),
            Component.newline(),
            Component.text("This request will expire in 60 seconds", NamedTextColor.GRAY)
        );

        target.sendMessage(message);
        player.sendMessage(Component.text("Teleport request sent to ").append(target.name()));
        return Command.SINGLE_SUCCESS;
    }
}
