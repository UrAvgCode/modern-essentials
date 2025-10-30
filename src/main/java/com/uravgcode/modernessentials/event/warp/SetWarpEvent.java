package com.uravgcode.modernessentials.event.warp;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class SetWarpEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String name;

    public SetWarpEvent(@NotNull Player player, @NotNull String name) {
        super(player);
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @SuppressWarnings("unused")
    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
