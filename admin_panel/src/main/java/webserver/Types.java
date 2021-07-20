package webserver;

public class Types {
    public static class ShopIncreaseStock {
        private String cardName;
        private int delta;

        public String getCardName() {
            return cardName;
        }

        public int getDelta() {
            return delta;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public void setDelta(int delta) {
            this.delta = delta;
        }
    }

    public static class ShopChangeStatus {
        private String cardName;
        private boolean forbidden;

        public String getCardName() {
            return cardName;
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
    }

}
