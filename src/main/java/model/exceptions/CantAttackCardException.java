package model.exceptions;

public class CantAttackCardException extends Exception {
    public CantAttackCardException() {
        super("you can't attack with this card");
    }
}
