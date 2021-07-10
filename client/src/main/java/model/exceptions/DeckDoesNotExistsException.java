package model.exceptions;

public class DeckDoesNotExistsException extends Exception {
    public DeckDoesNotExistsException(String name) {
        super(String.format("deck with name %s does not exist", name));
    }
}
