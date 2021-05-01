package model;

public class UserForScoreboard implements Comparable<UserForScoreboard> {
    private final String nickname;
    private final int score;

    public UserForScoreboard(User user) {
        this.score = user.getScore();
        this.nickname = user.getNickname();
    }

    public int getScore() {
        return score;
    }

    public String formatWithRank(int rank) {
        return "" + rank + this;
    }

    @Override
    public String toString() {
        return String.format("- %s: %d", this.nickname, this.score);
    }

    @Override
    public int compareTo(UserForScoreboard u) {
        int scoreDiff = this.score - u.score;
        if (scoreDiff == 0)
            return this.nickname.compareTo(u.nickname);
        return -scoreDiff;
    }
}
