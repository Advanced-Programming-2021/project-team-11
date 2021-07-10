package view;

import com.beust.jcommander.JCommander;
import model.database.CardLoader;
import model.database.UsersDatabase;
import model.exceptions.BooAnException;
import view.menus.RootMenu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        loader(parseCommandLineArgs(args)); // preload info we need in the program
        new RootMenu(args); // load the actual game
        saver(); // save users
    }

    private static CommandLineArguments parseCommandLineArgs(String[] args) {
        CommandLineArguments arguments = new CommandLineArguments();
        JCommander.newBuilder().addObject(arguments).build().parse(args);
        return arguments;
    }

    private static void loader(CommandLineArguments args) {
        try {
            CardLoader.loadCards(args.getMonstersConfigName(), args.getSpellConfigName());
        } catch (BooAnException ignored) {
        }
        try {
            UsersDatabase.connectToDatabase(args.getDatabase());
            UsersDatabase.loadUsers();
        } catch (SQLException ex) {
            System.out.println("Cannot connect/read database: " + ex.getMessage());
            System.exit(1);
        }
    }

    private static void saver() {
        try {
            UsersDatabase.saveUsers();
            UsersDatabase.closeDatabase();
            CardLoader.saveCards();
        } catch (SQLException ex) {
            System.out.println("Cannot save the database: " + ex.getMessage());
        }
    }
}
