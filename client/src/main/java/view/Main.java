package view;

import com.beust.jcommander.JCommander;
import controller.webserver.WebserverAddresses;
import kong.unirest.Unirest;
import model.database.CardLoader;
import model.exceptions.BooAnException;
import view.menus.RootMenu;

public class Main {
    public static void main(String[] args) {
        Unirest.config().defaultBaseUrl(WebserverAddresses.WEB_SERVER_HTTP_ADDRESS);
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
