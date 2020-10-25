package me.border.kalonbot.entities;

import me.border.kalonbot.Main;
import net.dv8tion.jda.api.entities.TextChannel;

public class Channels {

    public static TextChannel TWITCH;
    public static TextChannel TWITTER;
    public static TextChannel RULES;
    public static TextChannel ROLES;
    public static TextChannel WELCOME;

    public static void init() throws InterruptedException {
        TWITCH = getChannel("twitch", false);
        TWITTER = getChannel("twitter", false);
        RULES = getChannel("rules", false);
        ROLES = getChannel("roles", false);
        WELCOME = getChannel("welcome", false);
    }

    private static TextChannel getChannel(String value, boolean id) throws InterruptedException {
        if (id)
            return Main.getJDA().awaitReady().getTextChannelById(value);
        else
            return Main.getJDA().awaitReady().getTextChannelsByName(value, false).get(0);
    }
}
