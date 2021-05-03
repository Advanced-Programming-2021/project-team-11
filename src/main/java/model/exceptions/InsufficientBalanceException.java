package model.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super("not enough money");
    }
}
