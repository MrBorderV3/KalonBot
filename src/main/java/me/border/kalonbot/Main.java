package me.border.kalonbot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.border.kalonbot.utils.InviteManager;
import me.border.kalonbot.utils.KEmbed;
import me.border.kalonbot.commands.GetInvites;
import me.border.kalonbot.entities.Channels;
import me.border.kalonbot.entities.Emotes;
import me.border.kalonbot.entities.Roles;
import me.border.kalonbot.hook.TwitchHook;
import me.border.kalonbot.hook.TwitterHook;
import me.border.kalonbot.listeners.MemberJoinHandler;
import me.border.kalonbot.listeners.RolesReactHandler;
import me.border.kalonbot.listeners.VerificationReactHandler;
import me.border.kalonbot.storage.MySQLDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.awt.Color;
import java.util.*;

import static me.border.kalonbot.entities.Credentials.*;

public class Main {

    //private static Config config = new Config("config");

    private static JDA jda;
    private static CommandClient commandClient;
    private static Guild guild;

    private static MySQLDB db;

    public static Color KALON_COLOR = new Color(0x915A1C);

    public static void main(String[] args) throws LoginException, InterruptedException {
        startCommandClient();
        startJDA();
        // KALON ID 400207476747665411
        // TESTING ID 587030712381734952
        guild = jda.awaitReady().getGuildById(KALON_GUILD);
        Emotes.init();
        Channels.init();
        Roles.init();
        jda.awaitReady().addEventListener(new MemberJoinHandler(), new VerificationReactHandler(), new RolesReactHandler());

        db = new MySQLDB(HOSTNAME, DATABASE, USERNAME, PASSWORD, PORT);
        db.createInvitesTable();

        sendRoles();
        sendRules();

        TwitchHook.hook();
        TwitterHook.hook();

        InviteManager.init();
    }

    private static void sendRoles() throws InterruptedException {
        TextChannel rolesChannel = jda.awaitReady().getTextChannelsByName("roles", false).get(0);
        if (rolesChannel.getHistoryFromBeginning(2).complete().size() == 0){
            EmbedBuilder embedBuilder = new KEmbed();
            embedBuilder.setDescription("__**Kalon Esports Reaction Roles**__\n" +
                    "*Please react below with the categories that you're interested in, additionally, you may react again to remove the role.*\n\n" +
                    "**Overwatch:** <:overwatch:740597584925556787>\n\n" +
                    "**Valorant:** <:valorant:740597713401544774>\n\n" +
                    "**Twitch:** <:twitch:741665713789468712>\n\n" +
                    "**Twitter:** <:twitter:741665649193123951>");
            Message message = rolesChannel.sendMessage(embedBuilder.build()).complete();

            message.addReaction(Emotes.OVERWATCH).queue();
            message.addReaction(Emotes.VALORANT).queue();
            message.addReaction(Emotes.TWITCH).queue();
            message.addReaction(Emotes.TWITTER).queue();
        }
    }

    private static void sendRules() throws InterruptedException {
        TextChannel rulesChannel = jda.awaitReady().getTextChannelsByName("rules", false).get(0);
        if (rulesChannel.getHistoryFromBeginning(2).complete().size() == 0){
            EmbedBuilder embedBuilder = new KEmbed();
            embedBuilder.setDescription("__**Kalon Esports Rules**__\n\n" +
                    "1) Be respectful to all members!\n\n" +
                    "2) Don't be a jerk.\n\n" +
                    "3) Staff always have the last word. Even if it's not written in the rules always listen to what the staff members tell you.\n\n" +
                    "4) No insults, racism, sexism, homophobia, transphobia, and other kinds of discriminatory speech.\n\n" +
                    "5) Do not promote or advertise your content without permission from administrators.\n\n" +
                    "6) Do not impersonate Players, Staff, Moderators, Admins, or anything of that sort.\n\n" +
                    "7) Discussing and spreading damaging rumors about anyone is prohibited.\n\n" +
                    "8) Do not spam in any of the channels.\n\n" +
                    "9) We are against the use of link shorteners.\n\n" +
                    "10) Do not send out any personal or identifying information of anybody.");
            Message message = rulesChannel.sendMessage(embedBuilder.build()).complete();

            message.addReaction(Emotes.KALON).queue();
        }
    }

    private static void startCommandClient(){
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setPrefix("$");
        builder.setHelpWord("help");
        builder.setOwnerId("456802337030144011");
        builder.addCommands(new GetInvites());
        builder.setActivity(Activity.of(Activity.ActivityType.WATCHING, "#KalonWin"));

        commandClient = builder.build();
    }

    private static void startJDA() throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(KALON_TOKEN);
        builder.addEventListeners(commandClient);
        builder.setStatus(OnlineStatus.ONLINE);
        Collection<GatewayIntent> intents = new HashSet<>(Arrays.asList(GatewayIntent.values()));
        builder.setEnabledIntents(intents);

        Main.jda = builder.build();
    }

    /*public static Config getConfig(){
        return config;
    }*/

    public static Guild getGuild(){
        return guild;
    }

    public static MySQLDB getDB(){
        return db;
    }

    public static JDA getJDA(){
        return jda;
    }
}