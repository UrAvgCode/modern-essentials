package com.uravgcode.modernessentials.exception;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public final class BadSourceException extends CommandSyntaxException {
    private static final Message message = () -> "The source needs to be a command source stack";
    private static final CommandExceptionType type = new SimpleCommandExceptionType(message);

    public BadSourceException() {
        super(type, message);
    }
}
