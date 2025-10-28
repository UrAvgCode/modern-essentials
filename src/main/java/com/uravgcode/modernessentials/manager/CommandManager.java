package com.uravgcode.modernessentials.manager;

import com.google.common.reflect.ClassPath;
import com.uravgcode.modernessentials.command.PluginCommand;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class CommandManager {
    private CommandManager() {
    }

    public static List<@NotNull Class<? extends PluginCommand>> discoverCommands() {
        try {
            final var commandClasses = new ArrayList<Class<? extends PluginCommand>>();
            final var classLoader = CommandManager.class.getClassLoader();
            final var classPath = ClassPath.from(classLoader);

            final var packageName = "com.uravgcode.modernessentials.command";
            for (final var classInfo : classPath.getTopLevelClassesRecursive(packageName)) {
                final var clazz = classInfo.load();

                if (!PluginCommand.class.isAssignableFrom(clazz)) continue;
                if (Modifier.isAbstract(clazz.getModifiers())) continue;
                if (clazz.isInterface()) continue;

                final var commandClass = clazz.asSubclass(PluginCommand.class);
                commandClasses.add(commandClass);
            }

            return commandClasses;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void registerCommands(@NotNull ReloadableRegistrarEvent<@NotNull Commands> commands, @NotNull ComponentLogger logger) {
        final var registrar = commands.registrar();

        int count = 0;
        for (final var clazz : discoverCommands()) {
            try {
                final var constructor = clazz.getDeclaredConstructor();
                final var command = constructor.newInstance();
                registrar.register(command.build());
                count++;
            } catch (Exception exception) {
                logger.warn("Failed to register {}: {}", clazz.getSimpleName(), exception.getMessage());
            }
        }

        logger.info("Registered {} commands", count);
    }
}
