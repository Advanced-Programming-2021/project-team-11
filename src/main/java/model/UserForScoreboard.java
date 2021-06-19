package model;

public class UserForScoreboard implements Comparable<UserForScoreboard> {
    private final String nickname;
    private final int score;
    private int rank;

    public UserForScoreboard(User user) {
        this.score = user.getScore();
        this.nickname = user.getNickname();
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
        int scoreDiff = this.score - u.score;
        if (scoreDiff == 0)
            return this.nickname.compareTo(u.nickname);
        return -scoreDiff;
    }
}
