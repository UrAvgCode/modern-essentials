package com.uravgcode.modernessentials.command.weather;

@SuppressWarnings("unused")
public final class ThunderCommand  extends WeatherCommand {
    public ThunderCommand() {
        super("thunder",true, true, "commands.weather.set.thunder");
    }
}
