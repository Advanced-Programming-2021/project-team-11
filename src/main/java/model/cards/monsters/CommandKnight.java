package model.cards.monsters;

public class CommandKnight extends EffectMonsters {
    private static CommandKnight instance;

    private CommandKnight() {
        super("Command Knight");
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new CommandKnight();
    }

    @Override
    public void activateEffect() {

    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean haveEffectCondition() {
        return false;
    }
}
