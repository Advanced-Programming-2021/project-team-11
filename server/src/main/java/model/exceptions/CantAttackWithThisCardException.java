package model.exceptions;

public class CantAttackWithThisCardException extends Exception {
    public CantAttackWithThisCardException() {
        super("you can't attack with this card");
    }
}
