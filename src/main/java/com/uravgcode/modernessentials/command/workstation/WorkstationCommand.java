package com.uravgcode.modernessentials.command.workstation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.command.PluginCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public abstract class WorkstationCommand implements PluginCommand {
    protected final String name;
    protected final MenuType menu;

    protected WorkstationCommand(@NotNull String name, @NotNull MenuType menu) {
        this.name = name;
        this.menu = menu;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal(name)
            .requires(playerPermission("essentials." + name))
            .executes(this::openWorkstation)
            .build();
    }

    private int openWorkstation(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getSender() instanceof HumanEntity player) {
            menu.create(player, null).open();
        }
        return Command.SINGLE_SUCCESS;
    }
}
