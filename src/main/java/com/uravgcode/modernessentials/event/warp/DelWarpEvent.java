package com.uravgcode.modernessentials.event.warp;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class DelWarpEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String name;
    private final CommandSender sender;

    public DelWarpEvent(@NotNull CommandSender sender, @NotNull String name) {
        this.name = name;
        this.sender = sender;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull CommandSender getSender() {
        return sender;
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
