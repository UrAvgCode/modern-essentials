package com.uravgcode.modernessentials.event.home;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class HomeEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String name;

    public HomeEvent(Player player, String name) {
        super(player);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
