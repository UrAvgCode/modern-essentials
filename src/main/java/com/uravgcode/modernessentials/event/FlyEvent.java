package com.uravgcode.modernessentials.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class FlyEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public FlyEvent(@NotNull Player player) {
        super(player);
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
