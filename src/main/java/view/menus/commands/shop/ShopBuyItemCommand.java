package view.menus.commands.shop;

import model.exceptions.InvalidCommandException;
import view.menus.commands.Command;
import view.menus.commands.CommandUtils;

public class ShopBuyItemCommand implements Command {
    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(command, "shop buy");
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
