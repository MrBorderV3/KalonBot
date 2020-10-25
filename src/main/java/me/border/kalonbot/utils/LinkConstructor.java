package me.border.kalonbot.utils;

import java.math.BigInteger;

public class LinkConstructor {

    public static String twitch(String userName){
        return "https://www.twitch.tv/" + userName;
    }

    public static String twitterStatus(String userName, BigInteger tweetId){
        return  "https://twitter.com/" + userName + "/status/" + tweetId;
    }
}
