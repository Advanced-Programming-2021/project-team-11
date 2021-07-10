package model.exceptions;

import model.PlayableCard;

public class SpecialSummonNeededException extends Exception {
    private final PlayableCard toSummonCard;

    public SpecialSummonNeededException(PlayableCard toSummonCard) {
        this.toSummonCard = toSummonCard;
    }

    public PlayableCard getToSummonCard() {
        return toSummonCard;
    }
}
