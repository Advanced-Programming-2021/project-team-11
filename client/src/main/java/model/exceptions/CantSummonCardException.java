package model.exceptions;

public class CantSummonCardException extends Exception {
    public CantSummonCardException() {
        super("you can't summon this card");
    }
}
