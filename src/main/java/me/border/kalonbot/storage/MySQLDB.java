package me.border.kalonbot.storage;

public class MySQLDB extends IMySQLDB {

    public MySQLDB(String host, String database, String username, String password, int port) {
        super(host, database, username, password, port);
        System.out.println("Successfully connnected to MySQL Database at " + host + "|" + username);
    }

    public void createInvitesTable(){
        execute("CREATE TABLE IF NOT EXISTS invites(id VARCHAR(32) NOT NULL, uses INT NOT NULL, code VARCHAR(16) NOT NULL ,PRIMARY KEY(code));");
    }
}