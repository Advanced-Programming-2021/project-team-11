package model.enums;

public enum CoinFlipResult {
    TAIL,
    HEAD;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
