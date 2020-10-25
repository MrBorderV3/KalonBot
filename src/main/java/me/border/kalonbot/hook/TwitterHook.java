package me.border.kalonbot.hook;

import me.border.kalonbot.entities.Channels;
import me.border.kalonbot.utils.LinkConstructor;
import me.border.kalonbot.entities.Roles;
import me.border.kalonbot.entities.TwitterAccounts;
import winterwell.jtwitter.*;

import java.util.*;

import static me.border.kalonbot.entities.Credentials.*;

public class TwitterHook {

    private static Twitter twitter;

    public static void hook(){
        OAuthSignpostClient oAuth = new OAuthSignpostClient(API_KEY, API_SECRET, ACCESS_KEY, ACCESS_SECRET);
        /*System.out.println(oAuth.authorizeUrl().toString());
        String code = OAuthSignpostClient.askUser("What's the code?");
        oAuth.setAuthorizationCode(code);
        System.out.println(Arrays.toString(oAuth.getAccessToken()));*/
        twitter = new Twitter("KalonBot", oAuth);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TwitterStream twitterStream = new TwitterStream(twitter);
                List<Long> followedUsers = new ArrayList<>(Arrays.asList(TwitterAccounts.KALON, TwitterAccounts.MRBORDER, TwitterAccounts.HANS, TwitterAccounts.MOSHE));
                twitterStream.setFollowUsers(followedUsers);
                twitterStream.addListener(tweetListener);
                twitterStream.setAutoReconnect(true);
                twitterStream.connect();
            }
        }, 5000L, 600000);

    }

    private static AStream.IListen tweetListener = new AStream.IListen() {
        @Override
        public boolean processEvent(TwitterEvent twitterEvent) {
            return true;
        }

        @Override
        public boolean processSystemEvent(Object[] objects) {
            return true;
        }

        @Override
        public boolean processTweet(Twitter.ITweet iTweet) {
            User user = iTweet.getUser();
            String link = LinkConstructor.twitterStatus(user.getScreenName(), iTweet.getId());
            if (Channels.TWITTER.getHistory().getMessageById(Channels.TWITTER.getLatestMessageId()).getContentRaw().contains(user.getName()) && Channels.TWITTER.getHistory().getMessageById(Channels.TWITTER.getLatestMessageId()).getContentRaw().contains(link)) {
                return false;
            }
            Channels.TWITTER.sendMessage("<@&" + Roles.TWITTER.getId() + "> " + user.getName() + " Has tweeted!" + "\n" + link).queue();
            return true;
        }
    };
    public static Twitter getTwitter(){
        return twitter;
    }
}
