package com.uravgcode.modernessentials.exception;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Component;

public final class NoPlayerFoundException extends CommandSyntaxException {
    private static final Message message = Component.translatable("argument.entity.notfound.player");
    private static final CommandExceptionType type = new SimpleCommandExceptionType(message);

    public NoPlayerFoundException() {
        super(type, message);
    }
}
