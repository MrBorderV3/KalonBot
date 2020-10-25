package me.border.kalonbot.entities;

import me.border.kalonbot.Main;
import net.dv8tion.jda.api.entities.Role;

public class Roles {

    public static Role UNVERIFIED;
    public static Role OVERWATCH;
    public static Role VALORANT;
    public static Role TWITCH;
    public static Role TWITTER;

    public static void init() throws InterruptedException {
        UNVERIFIED = getRole("Unverified", false);
        OVERWATCH = getRole("OverwatchNotif", false);
        VALORANT = getRole("ValorantNotif", false);
        TWITCH = getRole("Twitch", false);
        TWITTER = getRole("Twitter", false);
    }

    private static Role getRole(String value, boolean id) throws InterruptedException {
        if (id)
            return Main.getJDA().awaitReady().getRoleById(value);
        else
            return Main.getJDA().awaitReady().getRolesByName(value, false).get(0);
    }
}
