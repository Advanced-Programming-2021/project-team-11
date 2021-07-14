package model.cards;

public class CardShopDetails {
    private String cardName;
    private int stock;
    private boolean forbidden;

    public String getCardName() {
        return cardName;
    }

    public int getStock() {
        return stock;
    }

    public boolean isForbidden() {
        return forbidden;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setForbidden(boolean forbidden) {
        this.forbidden = forbidden;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
