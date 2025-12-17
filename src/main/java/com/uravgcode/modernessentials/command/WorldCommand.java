package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@SuppressWarnings("unused")
public final class WorldCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("world")
            .requires(permission("essentials.world"))
            .then(Commands.argument("world", ArgumentTypes.world())
                .executes(this::execute))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var player = player(context);
        final var targetWorld = context.getArgument("world", World.class);

        final var location = player.getLocation();
        final var targetEnvironment = targetWorld.getEnvironment();
        final var currentEnvironment = player.getWorld().getEnvironment();

        location.setWorld(targetWorld);
        if (currentEnvironment == World.Environment.NETHER && targetEnvironment == World.Environment.NORMAL) {
            location.setX(location.getX() * 8.0);
            location.setZ(location.getZ() * 8.0);
        } else if (currentEnvironment == World.Environment.NORMAL && targetEnvironment == World.Environment.NETHER) {
            location.setX(location.getX() / 8.0);
            location.setZ(location.getZ() / 8.0);
        }

        player.teleportAsync(location, TeleportCause.COMMAND);
        player.sendMessage(Component.translatable(
            "commands.teleport.success.entity.single",
            player.name(),
            Component.text(targetWorld.getName())
        ));

        return Command.SINGLE_SUCCESS;
    }
}
