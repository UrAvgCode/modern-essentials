package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface CommandBuilder {
    LiteralCommandNode<CommandSourceStack> build();

    default @NotNull Predicate<CommandSourceStack> permission(@NotNull String permission) {
        return source -> source.getSender().hasPermission(permission);
    }

    default @NotNull Player player(@NotNull CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        if (context.getSource().getSender() instanceof Player player) return player;
        throw new RequiresPlayerException();
    }
}
