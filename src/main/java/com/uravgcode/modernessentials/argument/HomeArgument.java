package com.uravgcode.modernessentials.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.uravgcode.modernessentials.module.HomeModule;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class HomeArgument implements CustomArgumentType<String, String> {

    @Override
    public String parse(StringReader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof CommandSourceStack stack)) return Suggestions.empty();
        if (!(stack.getExecutor() instanceof Player player)) return Suggestions.empty();

        final var remaining = builder.getRemainingLowerCase();
        for (final var home : HomeModule.getHomes(player.getUniqueId()).tailMap(remaining).keySet()) {
            if (!home.toLowerCase().startsWith(remaining)) break;
            builder.suggest(home);
        }
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
