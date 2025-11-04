package com.uravgcode.modernessentials.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.modernessentials.ModernEssentials;
import com.uravgcode.modernessentials.update.UpdateChecker;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.maven.artifact.versioning.ComparableVersion;

@SuppressWarnings("unused")
public final class EssentialsCommand implements CommandBuilder {

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("essentials")
            .requires(permission("essentials.admin"))
            .then(Commands.literal("version")
                .executes(this::version))
            .then(Commands.literal("reload")
                .executes(this::reload))
            .build();
    }

    private int version(CommandContext<CommandSourceStack> context) {
        final var version = new ComparableVersion(ModernEssentials.instance().getPluginMeta().getVersion());
        final var sender = context.getSource().getSender();
        final var updateChecker = new UpdateChecker();

        sender.sendMessage(Component.text("Checking version, please wait...").decorate(TextDecoration.ITALIC));
        updateChecker.fetchLatestVersion().thenAccept(latestVersion -> {
            if (latestVersion.compareTo(version) > 0) {
                sender.sendMessage(Component.text("modern-essentials version: ")
                    .append(Component.text(version.toString(), NamedTextColor.GREEN)));
                sender.sendMessage(Component.text("Latest version: ")
                    .append(Component.text(latestVersion.toString(), NamedTextColor.GREEN)));
                sender.sendMessage(Component.text("Download: ")
                    .append(Component.text("Github", TextColor.color(0x59636e))
                        .clickEvent(ClickEvent.openUrl("https://github.com/UrAvgCode/modern-essentials/releases/latest")))
                    .append(Component.text(" Modrinth", TextColor.color(0x1bd96a))
                        .clickEvent(ClickEvent.openUrl("https://modrinth.com/plugin/modern-essentials/version/latest"))));
            } else if (latestVersion.compareTo(version) == 0) {
                sender.sendMessage(Component.text("modern-essentials version: ")
                    .append(Component.text(version.toString(), NamedTextColor.GREEN)));
                sender.sendMessage(Component.text("You are running the latest version", NamedTextColor.GREEN));
            } else {
                sender.sendMessage(Component.text("modern-essentials version: ")
                    .append(Component.text(version.toString(), NamedTextColor.GREEN)));
                sender.sendMessage(Component.text("Latest version: ")
                    .append(Component.text(latestVersion.toString(), NamedTextColor.GREEN)));
                sender.sendMessage(Component.text("You are running a newer version than the latest release", NamedTextColor.RED));
            }
        });

        return Command.SINGLE_SUCCESS;
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        ModernEssentials.instance().reload();
        final var sender = context.getSource().getSender();
        sender.sendMessage(Component.text("successfully reloaded config", NamedTextColor.GREEN));
        return Command.SINGLE_SUCCESS;
    }
}
