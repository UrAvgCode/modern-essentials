package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class InventorySeeCommand implements BaseCommand {

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("inventorysee")
            .requires(sender -> sender.getSender().hasPermission("essentials.inventorysee"))
            .then(Commands.argument("player", StringArgumentType.word())
                .suggests(InventorySeeCommand::getPlayerSuggestions)
                .executes(InventorySeeCommand::openInventory))
            .build()
        );
    }

    private static int openInventory(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getExecutor() instanceof Player player)) return 0;

        var targetName = context.getArgument("player", String.class);
        var server = context.getSource().getSender().getServer();
        var target = server.getPlayer(targetName);

        if (target != null) {
            player.openInventory(target.getInventory());
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
