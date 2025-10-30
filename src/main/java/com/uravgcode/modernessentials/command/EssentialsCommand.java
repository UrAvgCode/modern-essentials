package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.ModernEssentials;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("unused")
public final class EssentialsCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("essentials")
            .requires(permission("essentials.admin"))
            .then(Commands.literal("reload")
                .executes(this::reload))
            .build();
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        ModernEssentials.instance().reload();
        final var sender = context.getSource().getSender();
        sender.sendMessage(Component.text("successfully reloaded config", NamedTextColor.GREEN));
        return Command.SINGLE_SUCCESS;
    }
}
