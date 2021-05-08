package view;

import com.beust.jcommander.JCommander;
import model.database.CardLoader;
import view.menus.LoginMenu;

public class Main {
    public static void main(String[] args) {
        loader(parseCommandLineArgs(args)); // preload info we need in the program
        new LoginMenu(); // load the actual game
        // save everything before exiting
    }

    private static CommandLineArguments parseCommandLineArgs(String[] args) {
        CommandLineArguments arguments = new CommandLineArguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);
        return arguments;
    }

    private static void loader(CommandLineArguments args) {
        CardLoader.loadCards(args.getMonstersConfigName(), args.getSpellConfigName());
    }
}
