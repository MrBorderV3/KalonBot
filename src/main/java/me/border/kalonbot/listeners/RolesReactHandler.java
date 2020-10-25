package me.border.kalonbot.listeners;

import me.border.kalonbot.Main;
import me.border.kalonbot.entities.Channels;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

import static me.border.kalonbot.entities.Roles.*;

public class RolesReactHandler extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent e) {
        TextChannel textChannel = e.getTextChannel();
        if (textChannel.equals(Channels.ROLES)) {
            String id = e.getReactionEmote().getName();
            if (e.getUser().isBot())
                return;
            String userId = e.getUserId();
            Member member = e.getMember();
            List<Role> roles = member.getRoles();
            switch (id) {
                case "overwatch":
                    if (roles.contains(OVERWATCH)) {
                        Main.getGuild().removeRoleFromMember(userId, OVERWATCH).queue();
                        sendAfterRoleMessage(true, OVERWATCH.getName(), member);
                    } else {
                        Main.getGuild().addRoleToMember(userId, OVERWATCH).queue();
                        sendAfterRoleMessage(false, OVERWATCH.getName(), member);
                    }
                    break;
                case "valorant":
                    if (roles.contains(VALORANT)) {
                        Main.getGuild().removeRoleFromMember(userId, VALORANT).queue();
                        sendAfterRoleMessage(true, VALORANT.getName(), member);
                    } else {
                        Main.getGuild().addRoleToMember(userId, VALORANT).queue();
                        sendAfterRoleMessage(false, VALORANT.getName(), member);
                    }
                    break;
                case "twitch":
                    if (roles.contains(TWITCH)) {
                        Main.getGuild().removeRoleFromMember(userId, TWITCH).queue();
                        sendAfterRoleMessage(true, TWITCH.getName(), member);
                    } else {
                        Main.getGuild().addRoleToMember(userId, TWITCH).queue();
                        sendAfterRoleMessage(false, TWITCH.getName(), member);
                    }
                    break;
                case "twitter":
                    if (roles.contains(TWITTER)) {
                        Main.getGuild().removeRoleFromMember(userId, TWITTER).queue();
                        sendAfterRoleMessage(true, TWITTER.getName(), member);
                    } else {
                        Main.getGuild().addRoleToMember(userId, TWITTER).queue();
                        sendAfterRoleMessage(false, TWITTER.getName(), member);
                    }
                    break;
            }

            e.getReaction().removeReaction(e.getUser()).queue();
        }
    }

    private void sendAfterRoleMessage(boolean remove, String role, Member member){
        PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
        String added = remove ? "removed" : "added";
        privateChannel.sendMessage("Successfully " + added + " " + role + "!").queue();
    }
}