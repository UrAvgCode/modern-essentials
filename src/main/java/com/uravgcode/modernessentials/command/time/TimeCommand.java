package com.uravgcode.modernessentials.command.time;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.ModernEssentials;
import com.uravgcode.modernessentials.command.PluginCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class TimeCommand implements PluginCommand {
    protected final String name;
    protected final long time;

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

        sender.sendMessage(Component.translatable("commands.time.set", Component.text(time)));
        server.getGlobalRegionScheduler().execute(plugin, () -> world.setTime(time));

        return Command.SINGLE_SUCCESS;
    }
}
