package com.uravgcode.modernessentials.placeholder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Placeholders {
    private Placeholders() {
    }

    private static final TagResolver internalGlobalPlaceholders = TagResolver.resolver(
        globalPlaceholder("online", () -> Component.text(Bukkit.getOnlinePlayers().size())),
        globalPlaceholder("online_max", () -> Component.text(Bukkit.getMaxPlayers())),
        TagResolver.resolver("time", (arguments, context) -> {
            final var format = arguments.popOr("format expected").value();
            return Tag.inserting(context.deserialize(DateTimeFormatter.ofPattern(format).format(LocalDateTime.now())));
        })
    );

    private static final TagResolver internalAudiencePlaceholders = TagResolver.resolver(
        audiencePlaceholder("player", Player::getName),
        audiencePlaceholder("displayname", Player::displayName),
        audiencePlaceholder("uuid", Player::getUniqueId),
        audiencePlaceholder("ping", Player::getPing),
        audiencePlaceholder("health", player -> Math.round(player.getHealth())),
        audiencePlaceholder("world", player -> player.getWorld().getName())
    );

    public static TagResolver globalPlaceholders() {
        try {
            return TagResolver.resolver(
                io.github.miniplaceholders.api.MiniPlaceholders.audiencePlaceholders(),
                internalGlobalPlaceholders
            );
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
            return internalGlobalPlaceholders;
        }
    }

    public static TagResolver audiencePlaceholders() {
        try {
            return TagResolver.resolver(
                io.github.miniplaceholders.api.MiniPlaceholders.audiencePlaceholders(),
                internalAudiencePlaceholders
            );
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
            return internalAudiencePlaceholders;
        }
    }

    private static TagResolver globalPlaceholder(@TagPattern @NotNull String name, @NotNull Supplier<Component> supplier) {
        return TagResolver.resolver(name, (arguments, context) -> Tag.inserting(supplier.get()));
    }

    private static TagResolver audiencePlaceholder(@TagPattern @NotNull String name, @NotNull Function<Player, ?> handler) {
        return TagResolver.resolver(name, (arguments, context) -> {
            final var player = context.targetAsType(Player.class);
            final var result = handler.apply(player);
            final var content = result instanceof Component component
                ? component
                : Component.text(String.valueOf(result));
            return Tag.inserting(content);
        });
    }
}
