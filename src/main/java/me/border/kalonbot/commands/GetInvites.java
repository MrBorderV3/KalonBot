package me.border.kalonbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.border.kalonbot.utils.InviteManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class GetInvites extends Command {

    public GetInvites(){
        this.name = "getinvites";
        this.help = "Gets the invites of a player";
        this.arguments = "@(user)";
        this.requiredRole = "Admin";
    }

    @Override
    protected void execute(CommandEvent e) {
        if (e.getMessage().getContentRaw().split(" ").length != 2){
            e.reply("Illegal arguments! The correct usage is `$getinvites @(user)`");
            return;
        }
        new Thread(() -> {
            Message message = e.getMessage();
            Member member = message.getMentionedMembers().get(0);

            e.reply(member.getEffectiveName() + " Has " + InviteManager.getAmountOfUses(member) + " Invites");
        }).start();

    }
}
