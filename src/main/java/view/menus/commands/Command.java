package view.menus.commands;

import model.exceptions.InvalidCommandException;

public interface Command {
    /**
     * Remove command prefix from entered command and returns the command without it
     *
     * @param command The command to remove it
     * @return The prefix removed
     * @throws InvalidCommandException If the command is invalid, throws this exception
     */
    String removePrefix(String command) throws InvalidCommandException;
}
