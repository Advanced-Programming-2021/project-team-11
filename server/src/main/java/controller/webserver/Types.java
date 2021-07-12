package controller.webserver;

import model.User;

import java.util.Base64;

public class Types {
    public static class RegisterRequest {
        private String username, password, passwordConfirm, nickname;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getNickname() {
            return nickname;
        }

        public String getPasswordConfirm() {
            return passwordConfirm;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setPasswordConfirm(String passwordConfirm) {
            this.passwordConfirm = passwordConfirm;
        }
    }

    public static class ErrorMessage {
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public static ErrorMessage from(Throwable ex) {
            ErrorMessage message = new ErrorMessage();
            message.setError(ex.getMessage());
            return message;
        }

        public static ErrorMessage from(String string) {
            ErrorMessage message = new ErrorMessage();
            message.setError(string);
            return message;
        }
    }

    public static class LoginBody {
        private String username, password;

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class LoginResponse {
        private String token;

        public void setToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

    public static class ChangeNickname {
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    public static class ChangePassword {
        private String oldPassword, newPassword, newPasswordConfirm;

        public String getNewPassword() {
            return newPassword;
        }

        public String getNewPasswordConfirm() {
            return newPasswordConfirm;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public void setNewPasswordConfirm(String newPasswordConfirm) {
            this.newPasswordConfirm = newPasswordConfirm;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
    }

    public static class ProfilePicture {
        private String pic;

        public void setPic(byte[] pic) {
            this.pic = Base64.getEncoder().encodeToString(pic);
        }

        public byte[] getPic() {
            return Base64.getDecoder().decode(this.pic);
        }
    }

    public static class ProfileDetails {
        private String username, nickname, pic;
        private int score;

        public void setUser(User user) {
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.pic = Base64.getEncoder().encodeToString(user.getProfilePicBytes());
            this.score = user.getScore();
        }

        public String getNickname() {
            return nickname;
        }

        public String getUsername() {
            return username;
        }

        public int getScore() {
            return score;
        }

        public String getPic() {
            return pic;
        }
    }

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

    public static class ShopCard {
        private String card;

        public void setCard(String card) {
            this.card = card;
        }

        public String getCard() {
            return card;
        }
    }

    public static class DeckAddRemoveCard {
        private String deckName, cardName;
        private boolean side;

        public String getCardName() {
            return cardName;
        }

        public String getDeckName() {
            return deckName;
        }

        public boolean isSide() {
            return side;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public void setDeckName(String deckName) {
            this.deckName = deckName;
        }

        public void setSide(boolean side) {
            this.side = side;
        }
    }

    public static class DeckSwapCard {
        private String deckName, cardName;
        private boolean fromSide;

        public String getDeckName() {
            return deckName;
        }

        public String getCardName() {
            return cardName;
        }

        public boolean isFromSide() {
            return fromSide;
        }

        public void setDeckName(String deckName) {
            this.deckName = deckName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public void setFromSide(boolean fromSide) {
            this.fromSide = fromSide;
        }
    }
}
