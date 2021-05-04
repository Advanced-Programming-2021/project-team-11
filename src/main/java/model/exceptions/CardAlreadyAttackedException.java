package model.exceptions;

public class CardAlreadyAttackedException extends Exception {
    public CardAlreadyAttackedException() {
        super("this card already attacked");
    }
}
