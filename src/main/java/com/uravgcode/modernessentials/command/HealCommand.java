package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class HealCommand implements PluginCommand {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("heal")
            .requires(playerPermission("essentials.heal"))
            .then(Commands.argument("targets", ArgumentTypes.players())
                .requires(permission("essentials.heal.others"))
                .executes(HealCommand::healOther))
            .executes(HealCommand::heal)
            .build();
    }

    private static int heal(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (context.getSource().getSender() instanceof Player player) {
            final var attribute = player.getAttribute(Attribute.MAX_HEALTH);
            if (attribute != null) {
                final var health = player.getHealth();
                final var maxHealth = attribute.getValue();
                player.heal(maxHealth - health);
                player.sendMessage(Component.text("Healed ").append(player.name()));
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int healOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var targetResolver = context.getArgument("targets", PlayerSelectorArgumentResolver.class);
        final var targets = targetResolver.resolve(context.getSource());

        for (final var target : targets) {
            final var attribute = target.getAttribute(Attribute.MAX_HEALTH);
            if (attribute != null) {
                final var health = target.getHealth();
                final var maxHealth = attribute.getValue();
                target.heal(maxHealth - health);
            }
        }

        final var sender = context.getSource().getSender();
        if (targets.size() > 1) {
            sender.sendMessage(Component.text("Healed " + targets.size() + " players"));
        } else {
            sender.sendMessage(Component.text("Healed ").append(targets.getFirst().name()));
        }

        return Command.SINGLE_SUCCESS;
    }
}
