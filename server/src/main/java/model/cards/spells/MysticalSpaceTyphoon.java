package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;
import model.exceptions.CantUseSpellException;

public class MysticalSpaceTyphoon extends SpellCard {
    private final static String CARD_NAME = "Mystical space typhoon";
    private static MysticalSpaceTyphoon instance;

    private MysticalSpaceTyphoon() {
        super(CARD_NAME, SpellCardType.QUICK_PLAY, true);
    }

    public static MysticalSpaceTyphoon getInstance() {
        if (instance == null)
            instance = new MysticalSpaceTyphoon();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {}

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return rivalBoard.getSpellCardsList().size() != 0;
    }

    @Override
    public void throwConditionNotMadeException() throws CantUseSpellException {
        throw new CantUseSpellException("Rival doesn't have any cards!");
    }
}
