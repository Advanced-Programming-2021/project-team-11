package model.exceptions;

public class OnlySpellCardsAllowedException extends Exception {
    public OnlySpellCardsAllowedException() {
        super("activate effect is only for spell cards.");
    }
}
