package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.teleport.TeleportRequestEvent;
import com.uravgcode.modernessentials.event.teleport.TeleportResponseEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

@NullMarked
@CommandModule(name = "tpaccept")
public final class TeleportRequestModule extends PluginModule {
    private static final Duration LIFETIME = Duration.ofSeconds(60);

    public record Request(UUID sender, TeleportRequestEvent.Type type, long expiresAt) {
        boolean expired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }

    private final ConcurrentMap<UUID, LinkedHashMap<UUID, Request>> requests = new ConcurrentHashMap<>();

    public TeleportRequestModule(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTeleportRequest(TeleportRequestEvent event) {
        final var player = event.getPlayer();
        final var target = event.getTarget();
        final var type = event.getType();

        final var uuid = player.getUniqueId();
        final var expiresAt = System.currentTimeMillis() + LIFETIME.toMillis();
        requests.compute(target.getUniqueId(), (_, pending) -> {
            pending = pending == null ? new LinkedHashMap<>() : pending;
            pending.values().removeIf(Request::expired);
            pending.remove(uuid);
            pending.put(uuid, new Request(uuid, type, expiresAt));
            return pending;
        });

        final var message = Component.textOfChildren(
            player.name(),
            switch (type) {
                case TPA -> Component.text(" has requested to teleport to you ");
                case TPA_HERE -> Component.text(" has requested that you teleport to them ");
            },
            Component.text("[Accept]", NamedTextColor.GREEN)
                .clickEvent(ClickEvent.runCommand("/tpaccept " + player.getName()))
                .hoverEvent(Component.text("Click to accept")),
            Component.text(" "),
            Component.text("[Deny]", NamedTextColor.RED)
                .clickEvent(ClickEvent.runCommand("/tpdeny " + player.getName()))
                .hoverEvent(Component.text("Click to deny")),
            Component.newline(),
            Component.text("This request will expire in 60 seconds", NamedTextColor.GRAY)
        );

        target.sendMessage(message);
        player.sendMessage(Component.text("Teleport request sent to ").append(target.name()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTeleportResponse(TeleportResponseEvent event) {
        final var player = event.getPlayer();
        final var target = event.getTarget();
        final var response = event.getType();

        switch (response) {
            case ACCEPT -> {
                final var request = take(player, target);
                if (request == null) {
                    player.sendMessage(Component.text("You don't have any pending teleport requests", NamedTextColor.RED));
                    return;
                }

                final var requester = player.getServer().getPlayer(request.sender());
                if (requester == null) {
                    player.sendMessage(Component.text("That player isn't online anymore"));
                    return;
                }

                final var from = request.type() == TeleportRequestEvent.Type.TPA ? requester : player;
                final var to = request.type() == TeleportRequestEvent.Type.TPA ? player : requester;

                final var message = Component.translatable("commands.teleport.success.entity.single", from.name(), to.name());
                from.teleportAsync(to.getLocation(), TeleportCause.COMMAND);
                requester.sendMessage(message);
                player.sendMessage(message);
            }
            case DENY -> {
                final var request = take(player, target);
                if (request == null) {
                    player.sendMessage(Component.text("You don't have any pending teleport requests", NamedTextColor.RED));
                    return;
                }

                final var requester = player.getServer().getPlayer(request.sender());
                if (requester != null) {
                    requester.sendMessage(player.name().append(Component.text(" denied your teleport request")));
                }
                player.sendMessage(Component.text("Teleport request denied"));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        requests.remove(event.getPlayer().getUniqueId());
    }

    public @Nullable Request take(Player target, @Nullable Player sender) {
        final var result = new AtomicReference<@Nullable Request>();
        requests.computeIfPresent(target.getUniqueId(), (_, pending) -> {
            pending.values().removeIf(Request::expired);
            if (!pending.isEmpty()) {
                final var senderId = sender == null ? pending.lastEntry().getKey() : sender.getUniqueId();
                result.set(pending.remove(senderId));
            }
            return pending.isEmpty() ? null : pending;
        });
        return result.get();
    }
}
