package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("unused")
public final class RepairCommand implements PluginCommand {
    private static final Component NOTHING_TO_REPAIR = Component.text("nothing to repair", NamedTextColor.RED);
    private static final Component SUCCESSFULLY_REPAIRED = Component.text("successfully repaired ", NamedTextColor.GREEN);

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("repair")
            .requires(playerPermission("essentials.repair"))
            .then(Commands.literal("hand").executes(RepairCommand::repairHand))
            .then(Commands.literal("all").executes(RepairCommand::repairAll))
            .executes(RepairCommand::repairHand)
            .build()
        );
    }

    private static int repairHand(CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var item = player.getInventory().getItemInMainHand();

        if (repairItem(item)) {
            player.sendMessage(SUCCESSFULLY_REPAIRED.append(item.displayName()));
        } else {
            player.sendMessage(NOTHING_TO_REPAIR);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int repairAll(CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var repairedItems = new ArrayList<Component>();

        player.getInventory().forEach(item -> {
            if (repairItem(item)) {
                repairedItems.add(item.displayName());
            }
        });

        if (repairedItems.isEmpty()) {
            player.sendMessage(NOTHING_TO_REPAIR);
        } else {
            player.sendMessage(SUCCESSFULLY_REPAIRED.append(Component.join(JoinConfiguration.spaces(), repairedItems)));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static boolean repairItem(@Nullable ItemStack item) {
        if (item != null && item.getItemMeta() instanceof Damageable damageable && damageable.hasDamage()) {
            damageable.setDamage(0);
            item.setItemMeta(damageable);
            return true;
        }
        return false;
    }
}
