package com.uravgcode.modernessentials.exception;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;

public final class NoPlayerFoundException extends CommandSyntaxException {
    private static final Message message = MessageComponentSerializer.message().serialize(Component.translatable("argument.entity.notfound.player"));
    private static final CommandExceptionType type = new SimpleCommandExceptionType(message);

    public NoPlayerFoundException() {
        super(type, message);
    }
}
