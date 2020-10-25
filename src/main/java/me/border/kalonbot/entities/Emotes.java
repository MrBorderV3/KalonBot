package me.border.kalonbot.entities;

import me.border.kalonbot.Main;
import net.dv8tion.jda.api.entities.Emote;

public class Emotes {

    public static Emote KALON;
    public static Emote OVERWATCH;
    public static Emote VALORANT;
    public static Emote TWITCH;
    public static Emote TWITTER;

    public static void init() throws InterruptedException {
        KALON = getEmote("738405340336619557");
        OVERWATCH = getEmote("740597584925556787");
        VALORANT = getEmote("740597713401544774");
        TWITCH = getEmote("741665713789468712");
        TWITTER = getEmote("741665649193123951");
    }

    private static Emote getEmote(String id) throws InterruptedException {
        return Main.getJDA().awaitReady().getEmoteById(id);
    }
}
