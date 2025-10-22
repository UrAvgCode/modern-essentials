package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.uravgcode.modernessentials.ModernEssentials;
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
        final var plugin = ModernEssentials.instance();
        final var player = (Player) context.getSource().getSender();

        final var onlinePlayers = player.getServer().getOnlinePlayers();
        final var dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(VANISH_KEY)) {
            player.setInvisible(false);
            onlinePlayers.forEach(other -> {
                other.showPlayer(plugin, player);
                other.listPlayer(player);
            });
            dataContainer.remove(VANISH_KEY);
            player.sendMessage(Component.text("Vanish mode disabled", NamedTextColor.RED));
        } else {
            player.setInvisible(true);
            onlinePlayers.forEach(other -> {
                other.hidePlayer(plugin, player);
                other.unlistPlayer(player);
            });
            dataContainer.set(VANISH_KEY, PersistentDataType.BYTE, (byte) 1);
            player.sendMessage(Component.text("Vanish mode enabled", NamedTextColor.GREEN));
        }

        return Command.SINGLE_SUCCESS;
    }
}
