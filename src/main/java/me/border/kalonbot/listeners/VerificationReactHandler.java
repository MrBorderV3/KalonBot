package me.border.kalonbot.listeners;

import me.border.kalonbot.Main;
import me.border.kalonbot.entities.Channels;
import me.border.kalonbot.entities.Roles;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class VerificationReactHandler extends ListenerAdapter {


    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent e) {
        if (e.getChannel().equals(Channels.RULES)) {
            Member member = e.getMember();
            if (member == null)
                return;
            if (e.getUser().isBot())
                return;
            e.getReaction().removeReaction(member.getUser()).queue();
            if (member.getRoles().contains(Roles.UNVERIFIED)) {
                Main.getGuild().removeRoleFromMember(member, Roles.UNVERIFIED).queue();
                PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
                privateChannel.sendMessage("You have successfully verified! Have a good stay!\n\n#KalonWin").queue();
            }
        }
    }
}