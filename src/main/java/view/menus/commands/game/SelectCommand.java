package view.menus.commands.game;

import com.beust.jcommander.Parameter;
import model.enums.CardPlaceType;
import model.exceptions.InvalidCommandException;
import view.menus.commands.Command;
import view.menus.commands.CommandUtils;

public class SelectCommand implements Command {
    @Parameter(names = {"--monster", "-m"})
    private int monster;
    @Parameter(names = {"--spell", "-s"})
    private int spell;
    @Parameter(names = {"--hand", "-h"})
    private int hand;
    @Parameter(names = {"--opponent", "-o"})
    private boolean opponent;
    @Parameter(names = {"--field", "-f"})
    private boolean field;
    @Parameter(names = {"-d"})
    private boolean deselect;

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(command, "select");
    }

    public boolean isOpponent() {
        return opponent;
    }

    public boolean isDeselect() {
        return deselect;
    }

    public boolean isField() {
        return field;
    }

    public CardPlaceType getCardPlaceType() {
        if (monster != 0)
            return CardPlaceType.MONSTER;
        if (spell != 0)
            return CardPlaceType.SPELL;
        if (hand != 0)
            return CardPlaceType.HAND;
        if (field)
            return CardPlaceType.FIELD;
        return null;
    }

    public int getIndex() {
        if (monster != 0)
            return monster;
        if (spell != 0)
            return spell;
        if (hand != 0)
            return hand;
        return -1;
    }

    public boolean isSelectionValid() {
        if (!isValid())
            return false;
        CardPlaceType place = getCardPlaceType();
        if (place == null)
            return false;
        switch (place) {
            case MONSTER:
                return spell == 0 && hand == 0 && !field && monster <= 5 && monster > 0;
            case SPELL:
                return monster == 0 && hand == 0 && !field && spell <= 5 && spell > 0;
            case FIELD:
                return monster == 0 && spell == 0 && hand == 0;
            case HAND:
                return monster == 0 && spell == 0 && !field && hand <= 6 && hand > 0;
        }
        return false;
    }

    /**
     * Please note that other stuff like hand number and ... are checked in {@link #isSelectionValid()}
     *
     * @return True if the "command" is valid
     */
    @Override
    public boolean isValid() {
        if (deselect && (opponent || hand != 0 || monster != 0 || spell != 0 || field))
            return false;
        return !opponent || hand == 0;
    }
}
