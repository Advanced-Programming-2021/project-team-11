package view;

import com.google.gson.Gson;
import controller.webserver.Webserver;
import model.database.CardLoader;
import model.database.UsersDatabase;
import model.exceptions.BooAnException;
import model.exceptions.ConfigLoadingException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        StartupConfig config = loadServerConfig(args.length > 1 ? args[1] : "config.json");
        Webserver.startWebserver(config.getPort(), config.getAdminToken());
    }

    private static StartupConfig loadServerConfig(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            StartupConfig config = new Gson().fromJson(fileReader, StartupConfig.class);
            fileReader.close();
            return config;
        } catch (IOException ex) {
            throw new ConfigLoadingException(ex);
        }
    }

    private static void loader(StartupConfig config) {
        try {
            CardLoader.loadCards(config.getMonsters(), config.getSpells());
        } catch (BooAnException ignored) {
        }
        try {
            UsersDatabase.connectToDatabase(config.getDatabase());
            UsersDatabase.loadUsers();
        } catch (SQLException ex) {
            System.out.println("Cannot connect/read database: " + ex.getMessage());
            System.exit(1);
        }
    }
}
