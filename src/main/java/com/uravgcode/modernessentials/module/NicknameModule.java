package com.uravgcode.modernessentials.module;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.NicknameEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@CommandModule(name = "nickname")
public final class NicknameModule extends PluginModule implements PacketListener {
    private final NamespacedKey nicknameKey;
    private final Map<UUID, String> nicknames;
    private final PacketListenerCommon packetListener;

    public NicknameModule(JavaPlugin plugin) {
        super(plugin);
        this.nicknameKey = new NamespacedKey(plugin, "nickname");
        this.nicknames = new ConcurrentHashMap<>();
        this.packetListener = this.asAbstract(PacketListenerPriority.HIGH);
    }

    @Override
    public void enable() {
        if (!enabled) {
            enabled = true;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            PacketEvents.getAPI().getEventManager().registerListener(packetListener);
        }
    }

    @Override
    public void disable() {
        if (enabled) {
            enabled = false;
            HandlerList.unregisterAll(this);
            PacketEvents.getAPI().getEventManager().unregisterListener(packetListener);
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO_UPDATE) return;
        final var wrapper = new WrapperPlayServerPlayerInfoUpdate(event);

        for (final var playerInfo : wrapper.getEntries()) {
            final var profile = playerInfo.getGameProfile();
            final var nickname = nicknames.get(profile.getUUID());
            if (nickname == null) continue;

            profile.setName(nickname);
            event.markForReEncode(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNick(NicknameEvent event) {
        final var player = event.getPlayer();
        final var uuid = player.getUniqueId();
        final var dataContainer = player.getPersistentDataContainer();

        final var nickname = event.getNickname();
        if (nickname == null) {
            nicknames.remove(uuid);
            dataContainer.remove(nicknameKey);
        } else {
            nicknames.put(uuid, nickname);
            dataContainer.set(nicknameKey, PersistentDataType.STRING, nickname);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        final var dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(nicknameKey, PersistentDataType.STRING)) {
            nicknames.put(player.getUniqueId(), dataContainer.get(nicknameKey, PersistentDataType.STRING));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        nicknames.remove(event.getPlayer().getUniqueId());
    }
}
