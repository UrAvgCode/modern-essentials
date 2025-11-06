package com.uravgcode.modernessentials.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.uravgcode.modernessentials.exception.BadSourceException;
import com.uravgcode.modernessentials.exception.CannotTargetSelfException;
import com.uravgcode.modernessentials.exception.NoPlayerFoundException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class TargetPlayerArgument implements CustomArgumentType<Player, String> {

    @Override
    public Player parse(StringReader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S> Player parse(StringReader reader, S source) throws CommandSyntaxException {
        if (!(source instanceof CommandSourceStack stack)) throw new BadSourceException();

        final var name = reader.readString();
        final var sender = stack.getSender();
        final var player = sender.getServer().getPlayerExact(name);

        if (player == null) throw new NoPlayerFoundException();
        if (player.equals(sender)) throw new CannotTargetSelfException();

        return player;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack stack) {
            final var sender = stack.getSender();
            final var remaining = builder.getRemainingLowerCase();
            for (final var player : sender.getServer().getOnlinePlayers()) {
                final var name = player.getName();
                if (!name.toLowerCase().startsWith(remaining) || player.equals(sender)) continue;
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
}
