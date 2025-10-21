package com.uravgcode.modernessentials.command;

import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.NotNull;

public interface BaseCommand {
    void register(@NotNull Commands registrar);
}
