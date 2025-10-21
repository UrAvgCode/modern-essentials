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
public final class VanishCommand implements BaseCommand {
    public static final NamespacedKey VANISH_KEY = new NamespacedKey("modern-essentials", "vanish");

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("vanish")
            .requires(playerPermission("essentials.vanish"))
            .executes(VanishCommand::execute)
            .build()
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        var plugin = context.getSource().getSender().getServer().getPluginManager().getPlugin("modern-essentials");
        if (plugin == null) return 0;

        if (context.getSource().getExecutor() instanceof Player player) {
            var dataContainer = player.getPersistentDataContainer();

            if (dataContainer.has(VANISH_KEY)) {
                dataContainer.remove(VANISH_KEY);
                for (var other : player.getServer().getOnlinePlayers()) {
                    other.showPlayer(plugin, player);
                    other.listPlayer(player);
                }
                player.sendMessage(Component.text("Vanish mode disabled", NamedTextColor.RED));
            } else {
                for (var other : player.getServer().getOnlinePlayers()) {
                    other.hidePlayer(plugin, player);
                    other.unlistPlayer(player);
                }
                dataContainer.set(VANISH_KEY, PersistentDataType.BYTE, (byte) 1);
                player.sendMessage(Component.text("Vanish mode enabled", NamedTextColor.GREEN));
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
