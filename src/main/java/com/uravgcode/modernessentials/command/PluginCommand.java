package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface PluginCommand {
    LiteralCommandNode<CommandSourceStack> build();

    default @NotNull Predicate<CommandSourceStack> permission(@NotNull String permission) {
        return source -> source.getSender().hasPermission(permission);
    }

    default @NotNull Predicate<CommandSourceStack> playerPermission(@NotNull String permission) {
        return source -> source.getExecutor() instanceof Player player && player.hasPermission(permission);
    }
}
