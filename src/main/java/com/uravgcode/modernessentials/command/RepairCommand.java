package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class RepairCommand implements BaseCommand {

    @Override
    public void register(@NotNull Commands registrar) {
        registrar.register(Commands.literal("repair")
            .requires(playerPermission("essentials.repair"))
            .executes(RepairCommand::execute)
            .build()
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getExecutor() instanceof Player player) {
            var item = player.getInventory().getItemInMainHand();
            if (item.getItemMeta() instanceof Damageable damageable) {
                damageable.setDamage(0);
                item.setItemMeta(damageable);
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
