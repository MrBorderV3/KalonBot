package me.border.kalonbot.utils;

import me.border.kalonbot.Main;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static me.border.kalonbot.Main.getDB;

public class InviteManager {

    public static boolean refresh = true;

    private static Map<Member, List<Invite>> cachedInvites = new HashMap<>();

    public static void init(){
        initShutdownHook();
        initSaveTimer();
        initInviteTimer();
    }

    // ONLY USE THIS ASYNC
    public static int getAmountOfUses(Member member) {
        String id = member.getId();
        List<String> codes = new ArrayList<>();
        int finalUses = 0;

        List<Invite> cachedInvites = null;
        if (InviteManager.cachedInvites.containsKey(member)) {
            cachedInvites = InviteManager.cachedInvites.get(member);
        }


        if (cachedInvites != null) {
            for (Invite invite : cachedInvites) {
                String code = invite.getCode();
                ResultSet matchingInvite = getInviteFromDB(code);
                int uses = invite.getUses();

                try {
                    if (matchingInvite.next()) {
                        codes.add(code);
                    }

                    matchingInvite.getStatement().close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
                finalUses = finalUses + uses;
            }
        }

        ResultSet resultSet = getMemberInvitesFromDB(id);
        try {
            while (resultSet.next()) {
                String code = resultSet.getString("code");
                if (codes.contains(code)) {
                    continue;
                } else {
                    int uses = resultSet.getInt("uses");
                    finalUses = finalUses + uses;
                }
            }

            resultSet.getStatement().close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return finalUses;
    }


    private static void refreshInvites(){
        List<Invite> guildInvites = Main.getGuild().retrieveInvites().complete();

        for (Invite invite : guildInvites) {
            User user = invite.getInviter();
            if (user == null)
                continue;
            Member member = Main.getGuild().getMember(user);
            if (member == null)
                continue;

            if (cachedInvites.containsKey(member)){
                List<Invite> userInvites = cachedInvites.get(member);
                userInvites.add(invite);
                cachedInvites.replace(member, userInvites);
            } else {
                List<Invite> userInvites = new ArrayList<>();
                userInvites.add(invite);
                cachedInvites.put(member, userInvites);
            }
        }
    }


    private static ResultSet getInviteFromDB(String code){
        return getDB().executeQuery("SELECT * FROM invites WHERE code='" + code +  "';");
    }

    private static ResultSet getMemberInvitesFromDB(String id){
        return getDB().executeQuery("SELECT * FROM invites WHERE id='" + id +  "';");
    }

    private static void saveCache() {
        for (Map.Entry<Member, List<Invite>> entry : cachedInvites.entrySet()) {
            for (Invite invite : entry.getValue()) {
                Member member = entry.getKey();
                String id = member.getId();
                int uses = invite.getUses();
                String code = invite.getCode();

                if (uses != 0) {
                    getDB().executeUpdate("INSERT INTO invites (id, uses, code) VALUES ('" + id + "', " + uses + ", '" + code + "')" +
                            " ON DUPLICATE KEY UPDATE uses = " + uses + ";");
                }
            }
        }

        cachedInvites.clear();
    }

    private static void initShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(InviteManager::saveCache));
    }

    private static void initInviteTimer(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (refresh) {
                    refreshInvites();
                    refresh = false;
                }
            }
        };

        new Timer().scheduleAtFixedRate(task, 5000, 60000);
    }

    private static void initSaveTimer(){
        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        saveCache();
                    }
                }, 20000, 180000
        );
    }

}