package model.exceptions;

public class OutOfStockException extends Exception {
    public OutOfStockException() {
        super("this card is out of stock");
    }
}
