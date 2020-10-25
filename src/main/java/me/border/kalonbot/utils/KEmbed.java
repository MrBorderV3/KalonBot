package me.border.kalonbot.utils;

import me.border.kalonbot.Main;
import me.border.kalonbot.entities.Images;
import net.dv8tion.jda.api.EmbedBuilder;

public class KEmbed extends EmbedBuilder {

    public KEmbed(){
        super();
        setFooter("Kalon Esports", Images.KALON_LOGO);
        setKalonColor();
    }

    public void setKalonColor(){
        setColor(Main.KALON_COLOR);
    }
}
