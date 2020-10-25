package me.border.kalonbot.hook;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.TwitchClientHelper;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import me.border.kalonbot.entities.*;
import me.border.kalonbot.utils.LinkConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TwitchHook {

    private static TwitchClient twitchClient;

    public static void hook(){
        TwitchClientBuilder twitchClientBuilder = TwitchClientBuilder.builder().withEnableHelix(true).withDefaultAuthToken(new OAuth2Credential("twitch", Credentials.OAUTH)).withEventManager(new EventManager());
        twitchClient = twitchClientBuilder.build();
        TwitchClientHelper clientHelper = twitchClient.getClientHelper();

        twitchClient.getEventManager().registerEventHandler(new SimpleEventHandler());

        List<String> streamers = new ArrayList<>(Arrays.asList(TwitchAccounts.HANS, TwitchAccounts.MOSHE));
        clientHelper.enableStreamEventListener(streamers);

        Consumer<ChannelGoLiveEvent> eventConsumer = event -> {
            String channelName = event.getChannel().getName();
            if (channelName.equals(TwitchAccounts.HANS.toLowerCase())){
                channelName = TwitchAccounts.HANS;
            } else if (channelName.equals(TwitchAccounts.MOSHE.toLowerCase())){
                channelName = TwitchAccounts.MOSHE;
            }
            String streamTitle = event.getStream().getTitle();
            String link = LinkConstructor.twitch(channelName);
            Channels.TWITCH.sendMessage("<@&" + Roles.TWITCH.getId() + "> " + channelName + " Is streaming!\n\n**" + streamTitle + "**\n" + link).queue();
        };

        twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelGoLiveEvent.class, eventConsumer);
    }

    public static TwitchClient getTwitchClient(){
        return twitchClient;
    }
}
