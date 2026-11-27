package com.uravgcode.modernessentials.event.teleport;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TeleportRequestEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player target;
    private final Type type;

    public enum Type {
        TPA, TPA_HERE
    }

    public TeleportRequestEvent(Player player, Player target, Type type) {
        super(player);
        this.target = target;
        this.type = type;
    }

    public Player getTarget() {
        return target;
    }

    public Type getType() {
        return type;
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
