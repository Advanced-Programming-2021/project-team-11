package model.exceptions;

public class CantAttackToPlayerException extends Exception {
    public CantAttackToPlayerException() {
        super("you can't attack the opponent directly");
    }
}
