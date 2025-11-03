package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.CommandModule;
import com.uravgcode.modernessentials.event.mute.MuteEvent;
import com.uravgcode.modernessentials.event.mute.UnmuteEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@CommandModule(name = "mute")
public final class MuteModule extends PluginModule {
    private final NamespacedKey muteKey;

    public MuteModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        muteKey = new NamespacedKey(plugin, "mute");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMute(MuteEvent event) {
        final var player = event.getPlayer();
        final var dataContainer = player.getPersistentDataContainer();
        dataContainer.set(muteKey, PersistentDataType.BYTE, (byte) 1);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onUnmute(UnmuteEvent event) {
        final var player = event.getPlayer();
        final var dataContainer = player.getPersistentDataContainer();
        dataContainer.remove(muteKey);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        final var player = event.getPlayer();
        final var dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(muteKey)) {
            event.setCancelled(true);
            player.sendMessage(Component.translatable("chat.cannotSend", NamedTextColor.RED));
        }
    }
}
