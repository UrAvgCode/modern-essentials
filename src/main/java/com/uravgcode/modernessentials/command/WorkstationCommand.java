package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class WorkstationCommand implements PluginCommand {

    @Override
    public void register(@NotNull Commands registrar) {
        var workstations = Map.of(
            "workbench", MenuType.CRAFTING,
            "anvil", MenuType.ANVIL,
            "cartographytable", MenuType.CARTOGRAPHY_TABLE,
            "grindstone", MenuType.GRINDSTONE,
            "loom", MenuType.LOOM,
            "smithingtable", MenuType.SMITHING,
            "stonecutter", MenuType.STONECUTTER
        );

        workstations.forEach((name, menu) ->
            registrar.register(Commands.literal(name)
                .requires(playerPermission("essentials." + name))
                .executes(context -> openWorkstation(context, menu))
                .build()
            )
        );
    }

    private static int openWorkstation(CommandContext<CommandSourceStack> context, MenuType menu) {
        if (context.getSource().getExecutor() instanceof HumanEntity player) {
            menu.create(player, null).open();
        }
        return Command.SINGLE_SUCCESS;
    }
}
