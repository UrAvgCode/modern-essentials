package com.uravgcode.modernessentials.command.weather;

@SuppressWarnings("unused")
public final class RainCommand extends WeatherCommand {
    public RainCommand() {
        super("rain",true, false, "commands.weather.set.rain");
    }
}
