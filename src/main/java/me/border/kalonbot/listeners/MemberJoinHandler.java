package me.border.kalonbot.listeners;

import me.border.kalonbot.utils.InviteManager;
import me.border.kalonbot.Main;
import me.border.kalonbot.utils.KEmbed;
import me.border.kalonbot.entities.Channels;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MemberJoinHandler extends ListenerAdapter {

    private Role unverifiedRole;

    public MemberJoinHandler() {
        this.unverifiedRole = Main.getGuild().getRolesByName("Unverified", false).get(0);
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent e) {
        InviteManager.refresh = true;
        Member member = e.getMember();
        Channels.WELCOME.sendMessage(buildMessage(member)).queue();
        Main.getGuild().addRoleToMember(member, unverifiedRole).queue();
        PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
        privateChannel.sendMessage("Welcome to **Kalon Esports**\n\n" +
                "To get access to the server please go over <#581583824111009793> and react to the message once you've finished reading.\n\n" +
                "Have a good stay!").queue();
    }

    private MessageEmbed buildMessage(Member member){
        EmbedBuilder builder = new KEmbed();
        builder.setDescription("Welcome <@" + member.getId() + "> To the **Kalon Esports** discord!\n" +
                "\n" +
                ":pushpin: **INFORMATION** :pushpin:\n" +
                        "\n" +
                        "**• Website »** https://kalon-esports.com/\n" +
                        "**• Twitter »** https://twitter.com/Kalon_Esports\n" +
                        "**• Discord »** https://discord.kalon-esports.com/");

        return builder.build();
    }
}