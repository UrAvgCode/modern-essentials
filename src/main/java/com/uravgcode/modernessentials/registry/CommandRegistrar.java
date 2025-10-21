package com.uravgcode.modernessentials.registry;

import com.google.common.reflect.ClassPath;
import com.uravgcode.modernessentials.command.BaseCommand;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

public final class CommandRegistrar {
    private CommandRegistrar() {
    }

    public static void registerAll(@NotNull ReloadableRegistrarEvent<@NotNull Commands> commands, @NotNull ComponentLogger logger) {
        try {
            final var loader = CommandRegistrar.class.getClassLoader();
            final var classPath = ClassPath.from(loader);

            final var packageName = "com.uravgcode.modernessentials.command";
            final var classes = classPath.getTopLevelClasses(packageName).stream()
                .map(ClassPath.ClassInfo::load)
                .filter(BaseCommand.class::isAssignableFrom)
                .filter(Predicate.not(Class::isInterface))
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .toList();

            int count = 0;
            for (final var clazz : classes) {
                try {
                    final var command = clazz.asSubclass(BaseCommand.class).getDeclaredConstructor().newInstance();
                    command.register(commands.registrar());
                    count++;
                } catch (Exception ignored) {
                    logger.warn("Failed to register {}", clazz.getSimpleName());
                }
            }

            logger.info("Registered {} commands", count);
        } catch (IOException exception) {
            logger.error("Failed to register commands");
        }
    }
}
