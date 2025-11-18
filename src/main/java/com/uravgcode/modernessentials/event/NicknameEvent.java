package com.uravgcode.modernessentials.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NicknameEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @Nullable String nickname;

    public NicknameEvent(@NotNull Player player, @Nullable String nickname) {
        super(player);
        this.nickname = nickname;
    }

    public @Nullable String getNickname() {
        return nickname;
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
