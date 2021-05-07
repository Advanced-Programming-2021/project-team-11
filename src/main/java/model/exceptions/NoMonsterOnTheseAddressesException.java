package model.exceptions;

public class NoMonsterOnTheseAddressesException extends Exception {
    public NoMonsterOnTheseAddressesException() {
        super("there is no monster on one of these addresses");
    }
}
