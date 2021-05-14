package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;
import model.exceptions.CantSpecialSummonException;

public class MonsterReborn extends SpellCard {
    private final static String CARD_NAME = "Monster Reborn";
    private static MonsterReborn instance;

    private MonsterReborn() {
        super(CARD_NAME, SpellCardType.NORMAL, true);
    }

    public static MonsterReborn getInstance() {
        if (instance == null)
            instance = new MonsterReborn();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return (myBoard.getGraveyard().size() != 0 || rivalBoard.getGraveyard().size() != 0) && !myBoard.isMonsterZoneFull();
    }

    @Override
    public void throwConditionNotMadeException() throws CantSpecialSummonException {
        throw new CantSpecialSummonException("Graveyards are empty or your monster zone is full!");
    }
}
