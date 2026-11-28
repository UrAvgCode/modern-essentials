package com.uravgcode.modernessentials.event.teleport;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TeleportResponseEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @Nullable Player target;
    private final Type type;

    public enum Type {
        ACCEPT, DENY
    }

    public TeleportResponseEvent(Player player, @Nullable Player target, Type type) {
        super(player);
        this.target = target;
        this.type = type;
    }

    public @Nullable Player getTarget() {
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
