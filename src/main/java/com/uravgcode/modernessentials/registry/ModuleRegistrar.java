package com.uravgcode.modernessentials.registry;

import com.google.common.reflect.ClassPath;
import com.uravgcode.modernessentials.module.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class ModuleRegistrar {
    private static final List<@NotNull PluginModule> modules = new ArrayList<>();

    private ModuleRegistrar() {
    }

    public static void initializeModules(@NotNull JavaPlugin plugin) {
        final var logger = plugin.getComponentLogger();
        try {
            final var loader = plugin.getClass().getClassLoader();
            final var classPath = ClassPath.from(loader);

            final var packageName = "com.uravgcode.modernessentials.module";
            final var classes = classPath.getTopLevelClasses(packageName).stream()
                .map(ClassPath.ClassInfo::load)
                .filter(PluginModule.class::isAssignableFrom)
                .filter(Predicate.not(Class::isInterface))
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .toList();

            for (final var clazz : classes) {
                try {
                    final var moduleClass = clazz.asSubclass(PluginModule.class);
                    final var module = moduleClass.getConstructor(JavaPlugin.class).newInstance(plugin);
                    modules.add(module);
                } catch (Exception exception) {
                    logger.warn("Failed to initialize {}: {}", clazz.getSimpleName(), exception.getMessage());
                }
            }

            logger.info("Initialized {} modules", modules.size());
        } catch (IOException exception) {
            logger.error("Failed to initialize modules");
        }
    }

    public static void reloadModules() {
        modules.forEach(PluginModule::reload);
    }
}
