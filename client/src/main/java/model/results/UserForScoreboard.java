package model.results;

import model.User;

public class UserForScoreboard implements Comparable<UserForScoreboard> {
    private String nickname;
    private int score, rank;
    private boolean isOnline;

    public UserForScoreboard() {

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
