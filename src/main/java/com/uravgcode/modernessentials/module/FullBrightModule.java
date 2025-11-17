package com.uravgcode.modernessentials.module;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.world.chunk.LightData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateLight;
import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.FullBrightEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Server.CHUNK_DATA;
import static com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Server.UPDATE_LIGHT;

@NullMarked
@CommandModule(name = "fullbright")
public final class FullBrightModule extends PluginModule implements PacketListener {
    public final NamespacedKey fullBrightKey;
    private final Set<UUID> fullBrightPlayers;

    private final PacketListenerCommon packetListener;
    private final LightData lightData;

    public FullBrightModule(JavaPlugin plugin) {
        super(plugin);
        this.fullBrightKey = new NamespacedKey(plugin, "fullbright");
        this.fullBrightPlayers = ConcurrentHashMap.newKeySet();
        this.packetListener = this.asAbstract(PacketListenerPriority.HIGH);

        final int lightCount = 24;

        final var fullBrightSection = new byte[2048];
        Arrays.fill(fullBrightSection, (byte) 0xFF);

        final var lightArray = new byte[lightCount][];
        for (int i = 0; i < lightCount; ++i) lightArray[i] = fullBrightSection.clone();

        final var lightMask = new BitSet(lightCount);
        lightMask.set(0, lightCount);

        final var emptyLightMask = new BitSet(lightCount);

        this.lightData = new LightData(
            true,
            lightMask, lightMask,
            emptyLightMask, emptyLightMask,
            lightCount, lightCount,
            lightArray, lightArray
        );
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
        if (!fullBrightPlayers.contains(event.getUser().getUUID())) return;

        switch (event.getPacketType()) {
            case CHUNK_DATA -> {
                var wrapper = new WrapperPlayServerChunkData(event);
                wrapper.setLightData(lightData.clone());
                event.markForReEncode(true);
            }
            case UPDATE_LIGHT -> {
                var wrapper = new WrapperPlayServerUpdateLight(event);
                wrapper.setLightData(lightData.clone());
                event.markForReEncode(true);
            }
            default -> {
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFullBright(FullBrightEvent event) {
        final var player = event.getPlayer();
        final var uuid = player.getUniqueId();
        final var dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(fullBrightKey)) {
            fullBrightPlayers.remove(uuid);
            dataContainer.remove(fullBrightKey);
            player.sendMessage(Component.text("Fullbright disabled", NamedTextColor.RED));
        } else {
            fullBrightPlayers.add(uuid);
            dataContainer.set(fullBrightKey, PersistentDataType.BYTE, (byte) 1);
            player.sendMessage(Component.text("Fullbright enabled", NamedTextColor.GREEN));
        }

        sendLightUpdates(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        if (player.getPersistentDataContainer().has(fullBrightKey) && player.hasPermission("essentials.fullbright")) {
            fullBrightPlayers.add(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        fullBrightPlayers.remove(event.getPlayer().getUniqueId());
    }

    @SuppressWarnings("UnstableApiUsage")
    private void sendLightUpdates(Player player) {
        if (!(player instanceof CraftPlayer craftPlayer && player.getWorld() instanceof CraftWorld craftWorld)) return;
        final var lightEngine = craftWorld.getHandle().getLightEngine();

        for (final var chunkKey : player.getSentChunkKeys()) {
            final var chunkPosition = new ChunkPos(chunkKey);
            final var packet = new ClientboundLightUpdatePacket(chunkPosition, lightEngine, null, null);
            craftPlayer.getHandle().connection.send(packet);
        }
    }
}
