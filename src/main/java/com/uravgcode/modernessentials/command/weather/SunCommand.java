package com.uravgcode.modernessentials.command.weather;

@SuppressWarnings("unused")
public final class SunCommand  extends WeatherCommand{
    public SunCommand() {
        super("sun",false, false, "commands.weather.set.clear");
    }
}
