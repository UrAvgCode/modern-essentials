package com.uravgcode.modernessentials.command.workstation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.command.CommandBuilder;
import com.uravgcode.modernessentials.exception.RequiresPlayerException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public abstract class WorkstationCommand implements CommandBuilder {
    protected final String name;
    protected final MenuType menu;

    protected WorkstationCommand(@NotNull String name, @NotNull MenuType menu) {
        this.name = name;
        this.menu = menu;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal(name)
            .requires(permission("essentials." + name))
            .executes(this::openWorkstation)
            .build();
    }

    private int openWorkstation(CommandContext<CommandSourceStack> context) throws RequiresPlayerException {
        menu.create(player(context), null).open();
        return Command.SINGLE_SUCCESS;
    }
}
