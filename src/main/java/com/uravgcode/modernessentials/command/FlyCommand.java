package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class FlyCommand implements PluginCommand {
    public static final NamespacedKey FLY_KEY = new NamespacedKey("modern-essentials", "fly");

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("fly")
            .requires(playerPermission("essentials.fly"))
            .executes(FlyCommand::execute)
            .build()
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getExecutor() instanceof Player player) {
            var dataContainer = player.getPersistentDataContainer();

            if (dataContainer.has(FLY_KEY)) {
                if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
                dataContainer.remove(FLY_KEY);
                player.sendMessage(Component.text("Fly mode disabled", NamedTextColor.RED));
            } else {
                player.setAllowFlight(true);
                player.setFlying(true);
                dataContainer.set(FLY_KEY, PersistentDataType.BYTE, (byte) 1);
                player.sendMessage(Component.text("Fly mode enabled", NamedTextColor.GREEN));
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
