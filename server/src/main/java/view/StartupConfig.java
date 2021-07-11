package view;

public class StartupConfig {
    private String adminToken, monsters, spells, database;
    private int port;

    public int getPort() {
        return port;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public String getDatabase() {
        return database;
    }

    public String getMonsters() {
        return monsters;
    }

    public String getSpells() {
        return spells;
    }
}
