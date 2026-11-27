package com.uravgcode.modernessentials.teleport;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// keeps track of pending /tpa and /tpahere requests so they can be
// accepted either by clicking the chat button or with /tpaccept
@NullMarked
public final class TeleportRequests {
    public static final Duration LIFETIME = Duration.ofSeconds(60);

    public enum Type {TPA, TPA_HERE}

    public record Request(UUID sender, Type type, long expiresAt) {
        boolean expired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }

    // target -> (sender -> request), insertion order so "latest" is just the last one
    private static final Map<UUID, LinkedHashMap<UUID, Request>> requests = new ConcurrentHashMap<>();

    private TeleportRequests() {
    }

    public static void add(Player sender, Player target, Type type) {
        prune();
        final var expiresAt = System.currentTimeMillis() + LIFETIME.toMillis();
        final var pending = requests.computeIfAbsent(target.getUniqueId(), _ -> new LinkedHashMap<>());
        synchronized (pending) {
            // sending again just refreshes it and moves it to the end
            pending.remove(sender.getUniqueId());
            pending.put(sender.getUniqueId(), new Request(sender.getUniqueId(), type, expiresAt));
        }
    }

    // takes the request out of the map, so the same one can't be used twice
    // (no matter if it was the button or the command)
    public static @Nullable Request take(Player target, @Nullable Player sender) {
        final var pending = requests.get(target.getUniqueId());
        if (pending == null) return null;

        synchronized (pending) {
            pending.values().removeIf(Request::expired);

            Request request = null;
            if (sender != null) {
                request = pending.remove(sender.getUniqueId());
            } else {
                for (final var value : pending.values()) request = value;
                if (request != null) pending.remove(request.sender());
            }

            if (pending.isEmpty()) requests.remove(target.getUniqueId(), pending);
            return request;
        }
    }

    public static boolean accept(Player target, @Nullable Player sender) {
        final var request = take(target, sender);
        if (request == null) return false;

        final var requester = Bukkit.getPlayer(request.sender());
        if (requester == null) {
            target.sendMessage(Component.text("That player isn't online anymore"));
            return true;
        }

        // tpa: requester goes to target, tpahere: target goes to requester
        final var from = request.type() == Type.TPA ? requester : target;
        final var to = request.type() == Type.TPA ? target : requester;

        final var message = Component.translatable("commands.teleport.success.entity.single", from.name(), to.name());
        from.teleportAsync(to.getLocation(), TeleportCause.COMMAND);
        requester.sendMessage(message);
        target.sendMessage(message);
        return true;
    }

    public static boolean deny(Player target, @Nullable Player sender) {
        final var request = take(target, sender);
        if (request == null) return false;

        final var requester = Bukkit.getPlayer(request.sender());
        if (requester != null) {
            requester.sendMessage(target.name().append(Component.text(" denied your teleport request")));
        }
        target.sendMessage(Component.text("Teleport request denied"));
        return true;
    }

    // drop old stuff so the map doesn't keep growing on busy servers
    private static void prune() {
        requests.values().removeIf(pending -> {
            synchronized (pending) {
                pending.values().removeIf(Request::expired);
                return pending.isEmpty();
            }
        });
    }
}
