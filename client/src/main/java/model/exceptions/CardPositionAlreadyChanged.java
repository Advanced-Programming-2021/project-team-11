package model.exceptions;

public class CardPositionAlreadyChanged extends Exception {
    public CardPositionAlreadyChanged() {
        super("you already changed this card position in this turn");
    }
}
