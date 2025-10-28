package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class EnderChestCommand implements PluginCommand {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("enderchest")
            .requires(playerPermission("essentials.enderchest"))
            .then(Commands.argument("player", StringArgumentType.word())
                .requires(playerPermission("essentials.enderchest.others"))
                .suggests(EnderChestCommand::getPlayerSuggestions)
                .executes(EnderChestCommand::openOtherEnderChest))
            .executes(EnderChestCommand::openEnderChest)
            .build();
    }

    private static int openEnderChest(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getExecutor() instanceof Player player) {
            player.openInventory(player.getEnderChest());
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int openOtherEnderChest(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getExecutor() instanceof Player player)) return 0;

        var targetName = context.getArgument("player", String.class);
        var server = context.getSource().getSender().getServer();
        var target = server.getPlayer(targetName);

        if (target != null) {
            player.openInventory(target.getEnderChest());
        } else {
            player.sendMessage(Component.translatable("argument.entity.notfound.player", NamedTextColor.RED));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> getPlayerSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        var server = context.getSource().getSender().getServer();
        server.getOnlinePlayers().stream().map(Player::getName).forEach(builder::suggest);
        return builder.buildFuture();
    }
}
