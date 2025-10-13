package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.uravgcode.modernessentials.ModernEssentials;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class EssentialsCommand {

    public static void registerCommands(Commands registrar) {
        registrar.register(Commands.literal("essentials")
            .requires(sender -> sender.getSender().hasPermission("essentials.admin"))
            .then(Commands.literal("reload").executes(EssentialsCommand::reload))
            .build()
        );
    }

    private static int reload(CommandContext<CommandSourceStack> context) {
        var plugin = context.getSource().getSender().getServer().getPluginManager().getPlugin("modern-essentials");
        if (!(plugin instanceof ModernEssentials modernEssentials)) return 0;

        modernEssentials.reload();
        context.getSource().getSender().sendMessage(Component.text("successfully reloaded config", NamedTextColor.GREEN));
        return Command.SINGLE_SUCCESS;
    }
}
