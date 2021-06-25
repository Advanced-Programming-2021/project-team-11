package model;

public class UserForScoreboard implements Comparable<UserForScoreboard> {
    private final String nickname;
    private final int score;
    private int rank;

    public UserForScoreboard(User user) {
        this.nickname = user.getNickname();
        this.score = user.getScore();
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

    @Override
    public String toString() {
        return String.format("- %s: %d", this.nickname, this.score);
    }

    @Override
    public int compareTo(UserForScoreboard u) {
        return this.score != u.score ? u.score - this.score: this.nickname.compareTo(u.nickname);
    }
}
