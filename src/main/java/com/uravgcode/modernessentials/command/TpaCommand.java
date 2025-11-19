package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.argument.OtherPlayerArgument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.time.Duration;

@SuppressWarnings("unused")
public final class TpaCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("tpa")
            .requires(permission("essentials.tpa"))
            .then(Commands.argument("target", new OtherPlayerArgument())
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        final var target = context.getArgument("target", Player.class);

        final var options = ClickCallback.Options.builder()
            .lifetime(Duration.ofSeconds(60))
            .uses(1)
            .build();

        final var message = Component.textOfChildren(
            player.name(),
            Component.text(" has requested to teleport to you "),
            Component.text("[Accept]", NamedTextColor.GREEN)
                .clickEvent(ClickEvent.callback(audience -> {
                    final var teleportMessage = Component.translatable("commands.teleport.success.entity.single", player.name(), target.name());
                    player.teleport(target);
                    player.sendMessage(teleportMessage);
                    target.sendMessage(teleportMessage);
                }, options))
                .hoverEvent(Component.translatable("Click to accept")),
            Component.newline(),
            Component.text("This request will expire in 60 seconds", NamedTextColor.GRAY)
        );

        target.sendMessage(message);
        player.sendMessage(Component.text("Teleport request sent to ").append(target.name()));
        return Command.SINGLE_SUCCESS;
    }
}
