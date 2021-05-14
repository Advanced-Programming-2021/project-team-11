package model.exceptions;

public class CantSpecialSummonException extends Exception {
    public CantSpecialSummonException() {
        super("there is no way you could special summon a monster");
    }

    public CantSpecialSummonException(String message) {
        super("there is no way you could special summon a monster: " + message);
    }
}
