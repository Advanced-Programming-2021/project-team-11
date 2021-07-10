package model.exceptions;

public class CantFlipSummonException extends Exception {
    public CantFlipSummonException() {
        super("you can’t flip summon this card");
    }
}
