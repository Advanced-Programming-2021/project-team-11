package model.exceptions;

public class DeckExistsException extends Exception {
    public DeckExistsException(String name) {
        super(String.format("deck with name %s already exists", name));
    }
}
