package model.results;

import controller.webserver.routes.PresenceController;
import model.User;

public class UserForScoreboard implements Comparable<UserForScoreboard> {
    private String nickname;
    private int score, rank;
    private boolean isOnline;

    public UserForScoreboard() {

    }

    public UserForScoreboard(User user) {
        this.nickname = user.getNickname();
        this.score = user.getScore();
        this.isOnline = PresenceController.isUserOnline(user.getUsername());
    }

    public String getNickname() {
        return nickname;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public String toString() {
        return String.format("- %s: %d", this.nickname, this.score);
    }

    @Override
    public int compareTo(UserForScoreboard u) {
        return this.score != u.score ? u.score - this.score : this.nickname.compareTo(u.nickname);
    }
}
