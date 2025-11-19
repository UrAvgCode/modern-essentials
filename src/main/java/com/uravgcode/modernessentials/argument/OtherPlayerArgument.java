package com.uravgcode.modernessentials.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.uravgcode.modernessentials.exception.BadSourceException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class OtherPlayerArgument implements CustomArgumentType<Player, PlayerSelectorArgumentResolver> {
    private static final SimpleCommandExceptionType INVALID_TARGET = new SimpleCommandExceptionType(() -> "You cannot target yourself");

    @Override
    public Player parse(StringReader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S> Player parse(StringReader reader, S source) throws CommandSyntaxException {
        if (!(source instanceof CommandSourceStack stack)) throw new BadSourceException();

        final var sender = stack.getSender();
        final var player = getNativeType().parse(reader).resolve(stack).getFirst();
        if (player.equals(sender)) throw INVALID_TARGET.create();

        return player;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof CommandSourceStack stack)) return Suggestions.empty();

        final var senderName = stack.getSender().getName();
        return ArgumentTypes.player().listSuggestions(context, builder)
            .thenApply(suggestions -> {
                suggestions.getList().removeIf(suggestion -> suggestion.getText().equalsIgnoreCase(senderName));
                return suggestions;
            });
    }

    @Override
    public ArgumentType<PlayerSelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.player();
    }
}
