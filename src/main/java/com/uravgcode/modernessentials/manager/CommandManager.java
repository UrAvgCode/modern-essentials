package com.uravgcode.modernessentials.manager;

import com.google.common.reflect.ClassPath;
import com.uravgcode.modernessentials.command.CommandBuilder;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public final class CommandManager {
    private final ComponentLogger logger;
    private final File configFile;

    public CommandManager(@NotNull BootstrapContext context) {
        this.logger = context.getLogger();
        this.configFile = context.getDataDirectory().resolve("config.yml").toFile();
    }

    public void registerCommands(ReloadableRegistrarEvent<@NotNull Commands> commands) {
        final var config = YamlConfiguration.loadConfiguration(configFile);
        final var disabledCommands = Set.copyOf(config.getStringList("commands.disabled"));

        int count = 0;
        for (final var clazz : discoverCommands()) {
            try {
                final var constructor = clazz.getDeclaredConstructor();
                final var pluginCommand = constructor.newInstance();

                final var command = pluginCommand.build();
                final var literal = command.getLiteral();
                if (disabledCommands.contains(literal)) continue;

                commands.registrar().register(command);
                count++;
            } catch (Exception exception) {
                logger.warn("Failed to register {}: {}", clazz.getSimpleName(), exception.getMessage());
            }
        }

        logger.info("Registered {} commands", count);
    }

    private List<@NotNull Class<? extends CommandBuilder>> discoverCommands() {
        final var commandClasses = new ArrayList<Class<? extends CommandBuilder>>();
        try {
            final var classLoader = getClass().getClassLoader();
            final var classPath = ClassPath.from(classLoader);

            final var packageName = "com.uravgcode.modernessentials.command";
            for (final var classInfo : classPath.getTopLevelClassesRecursive(packageName)) {
                final var clazz = classInfo.load();

                if (!CommandBuilder.class.isAssignableFrom(clazz)) continue;
                if (Modifier.isAbstract(clazz.getModifiers())) continue;
                if (clazz.isInterface()) continue;

                final var commandClass = clazz.asSubclass(CommandBuilder.class);
                commandClasses.add(commandClass);
            }
        } catch (Exception exception) {
            logger.error("Failed to load command classes", exception);
        }

        return commandClasses;
    }
}
