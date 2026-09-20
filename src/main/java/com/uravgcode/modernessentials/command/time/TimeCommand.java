package com.uravgcode.modernessentials.command.time;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.ModernEssentials;
import com.uravgcode.modernessentials.command.CommandBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public abstract class TimeCommand implements CommandBuilder {
    private final String name;
    private final long time;

    protected TimeCommand(@NotNull String name, long time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal(name)
            .requires(permission("essentials.time"))
            .executes(this::setTime)
            .build();
    }

    private int setTime(CommandContext<CommandSourceStack> context) {
        final var plugin = ModernEssentials.instance();
        final var sender = context.getSource().getSender();
        final var server = sender.getServer();

        final var executor = context.getSource().getExecutor();
        final var world = executor != null ? executor.getWorld() : server.getRespawnWorld();

        server.getGlobalRegionScheduler().execute(plugin, () -> {
            try {
                world.setTime(time);
                sender.sendMessage(Component.translatable("commands.time.set.time_marker", Component.text(world.getKey().asString()), Component.text("minecraft:" + name)));
            } catch (IllegalArgumentException _) {
                sender.sendMessage(Component.translatable("commands.time.no_default_clock", Component.text(world.getKey().asString())).color(NamedTextColor.RED));
            }
        });

        return Command.SINGLE_SUCCESS;
    }
}
