package view;

import com.beust.jcommander.JCommander;
import kong.unirest.Unirest;
import model.database.CardLoader;
import model.exceptions.BooAnException;
import view.menus.RootMenu;

public class Main {
    public static void main(String[] args) {
        Unirest.config().defaultBaseUrl("http://127.0.0.1:8888");
        loader(parseCommandLineArgs(args)); // preload info we need in the program
        new RootMenu(args); // load the actual game
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
    }
}
