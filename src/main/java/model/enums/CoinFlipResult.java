package model.enums;

public enum CoinFlipResult {
    HEAD,
    TAIL;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
