package model.exceptions;

public class RitualSummonNotPossibleException extends Exception {
    public RitualSummonNotPossibleException() {
        super("there is no way you could ritual summon a monster\n");
    }
}
