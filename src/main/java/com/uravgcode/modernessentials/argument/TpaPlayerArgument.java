package com.uravgcode.modernessentials.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class TpaPlayerArgument extends OtherPlayerArgument {
    private final SimpleCommandExceptionType invalidTargetExceptionType = new SimpleCommandExceptionType(() -> "Target cannot accept teleport requests");

    @Override
    public <S> Player parse(StringReader reader, S source) throws CommandSyntaxException {
        final var target = super.parse(reader, source);
        if (!target.hasPermission("essentials.tpa.accept")) throw invalidTargetExceptionType.create();
        return target;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return super.listSuggestions(context, builder).thenApply(suggestions -> {
            suggestions.getList().removeIf(suggestion -> {
                final var player = Bukkit.getPlayerExact(suggestion.getText());
                return player != null && !player.hasPermission("essentials.tpa.accept");
            });
            return suggestions;
        });
    }
}
