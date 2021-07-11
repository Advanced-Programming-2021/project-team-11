package model.exceptions;

public class SpellCardZoneFullException extends Exception {
    public SpellCardZoneFullException() {
        super("spell card zone is full");
    }
}
