package com.uravgcode.modernessentials.exception;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;

public final class BadSourceException extends CommandSyntaxException {
    private static final Message message = MessageComponentSerializer.message().serialize(Component.text("The source needs to be a command source stack"));
    private static final CommandExceptionType type = new SimpleCommandExceptionType(message);

    public BadSourceException() {
        super(type, message);
    }
}
