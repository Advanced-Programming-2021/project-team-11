package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class ScannerCard extends InitializableEffectMonsters {
    private static ScannerCard instance;
    private final static String CARD_NAME = "Scanner";

    private ScannerCard() {
        super(CARD_NAME);
    }

    public static ScannerCard getInstance() {
        if (instance == null)
            instance = new ScannerCard();
        return instance;
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    /**
     * Mimics rival card to this card
     *
     * @param myBoard           Has no effect
     * @param rivalBoard        Has no effect
     * @param thisCard          This card
     * @param rivalCard         The card to mimic
     * @param activationCounter Has no effect
     */
    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        thisCard.setMimicCard(rivalCard.getCard());
    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return !(thisCard.getCard() instanceof ScannerCard) && !rivalBoard.getGraveyard().isEmpty();
    }
}
