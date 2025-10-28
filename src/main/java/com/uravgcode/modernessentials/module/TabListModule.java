package com.uravgcode.modernessentials.module;

import com.uravgcode.modernessentials.annotation.ConfigModule;
import com.uravgcode.modernessentials.annotation.ConfigValue;
import com.uravgcode.modernessentials.placeholder.Placeholders;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@ConfigModule(path = "tab-list")
public final class TabListModule extends PluginModule {
    private final MiniMessage miniMessage;
    private ScheduledTask task = null;

    @ConfigValue(path = "tab-list.format")
    private String format = "<player>";

    @ConfigValue(path = "tab-list.header")
    private String headerString = "";

    @ConfigValue(path = "tab-list.footer")
    private String footerString = "";

    @ConfigValue(path = "tab-list.refresh-interval")
    private long refreshInterval = 20L;

    public TabListModule(@NotNull JavaPlugin plugin) {
        super(plugin);
        miniMessage = MiniMessage.builder().tags(TagResolver.resolver(
            TagResolver.standard(),
            Placeholders.globalPlaceholders(),
            Placeholders.audiencePlaceholders()
        )).build();
    }

    @Override
    public void enable() {
        if (task != null) return;
        enabled = true;

        final var scheduler = plugin.getServer().getGlobalRegionScheduler();
        task = scheduler.runAtFixedRate(plugin, this::updatePlayerList, 1L, refreshInterval);
    }

    @Override
    public void disable() {
        if (task == null) return;
        enabled = false;

        task.cancel();
        task = null;
    }

    private void updatePlayerList(final ScheduledTask ignored) {
        final var server = plugin.getServer();
        for (final var player : server.getOnlinePlayers()) {
            final var name = miniMessage.deserialize(format, player);
            final var header = miniMessage.deserialize(headerString, player);
            final var footer = miniMessage.deserialize(footerString, player);

            player.playerListName(name);
            player.sendPlayerListHeaderAndFooter(header, footer);
        }
    }
}
