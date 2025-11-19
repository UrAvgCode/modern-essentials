package com.uravgcode.modernessentials.exception;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Component;

public final class RequiresPlayerException extends CommandSyntaxException {
    private static final Message message = Component.translatable("permissions.requires.player");
    private static final CommandExceptionType type = new SimpleCommandExceptionType(message);

    public RequiresPlayerException() {
        super(type, message);
    }
}
