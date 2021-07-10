package model.exceptions;

public class MonsterCardZoneFullException extends Exception {
    public MonsterCardZoneFullException() {
        super("monster card zone is full");
    }
}
