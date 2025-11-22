package com.uravgcode.modernessentials.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.uravgcode.modernessentials.module.WarpModule;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class WarpArgument implements CustomArgumentType<String, String> {

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
        final var remaining = builder.getRemainingLowerCase();
        for (final var warp : WarpModule.getWarps().tailMap(remaining).keySet()) {
            if (!warp.toLowerCase().startsWith(remaining)) break;
            builder.suggest(warp);
        }
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
