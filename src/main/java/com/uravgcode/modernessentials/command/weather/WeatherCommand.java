package com.uravgcode.modernessentials.command.weather;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.ModernEssentials;
import com.uravgcode.modernessentials.command.PluginCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class WeatherCommand implements PluginCommand {
    protected final String name;
    protected final boolean storm;
    protected final boolean thunder;
    protected final String key;

    protected WeatherCommand(@NotNull String name, boolean storm, boolean thunder, @NotNull String key) {
        this.name = name;
        this.storm = storm;
        this.thunder = thunder;
        this.key = key;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal(name)
            .requires(permission("essentials.weather"))
            .executes(this::setWeather)
            .build();
    }

    private int setWeather(CommandContext<CommandSourceStack> context) {
        final var plugin = ModernEssentials.instance();
        final var sender = context.getSource().getSender();
        final var server = sender.getServer();

        final var executor = context.getSource().getExecutor();
        final var world = executor != null ? executor.getWorld() : server.getRespawnWorld();

        sender.sendMessage(Component.translatable(key));
        server.getGlobalRegionScheduler().execute(plugin, () -> {
            world.setStorm(storm);
            world.setThundering(thunder);
        });

        return Command.SINGLE_SUCCESS;
    }
}
