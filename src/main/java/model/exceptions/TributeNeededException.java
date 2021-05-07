package model.exceptions;

public class TributeNeededException extends Exception {
    private final int neededTributes;

    public TributeNeededException(int neededTributes) {
        super(String.format("%d tribute(s) needed", neededTributes));
        this.neededTributes = neededTributes;
    }

    public int getNeededTributes() {
        return neededTributes;
    }
}
