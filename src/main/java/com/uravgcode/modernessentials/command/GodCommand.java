package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class GodCommand implements PluginCommand {
    public static final NamespacedKey GOD_KEY = new NamespacedKey("modern-essentials", "god");

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("god")
            .requires(playerPermission("essentials.god"))
            .executes(GodCommand::execute)
            .build()
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getExecutor() instanceof Player player) {
            var dataContainer = player.getPersistentDataContainer();

            if (dataContainer.has(GOD_KEY)) {
                dataContainer.remove(GOD_KEY);
                player.sendMessage(Component.text("God mode disabled", NamedTextColor.RED));
            } else {
                player.setHealth(20);
                player.setFoodLevel(20);
                dataContainer.set(GOD_KEY, PersistentDataType.BYTE, (byte) 1);
                player.sendMessage(Component.text("God mode enabled", NamedTextColor.GREEN));
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
