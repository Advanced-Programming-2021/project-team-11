package model.game;

public class GameEndResults {
    private final int roundNumbers, player1MaxHp, player2MaxHp;
    private final boolean player1Won;

    public GameEndResults(int player1MaxHp, int player2MaxHp, boolean player1Won, int roundNumbers) {
        this.player1MaxHp = player1MaxHp;
        this.player2MaxHp = player2MaxHp;
        this.roundNumbers = roundNumbers;
        this.player1Won = player1Won;
    }

    public int getPlayer1Score() {
        return roundNumbers * (player1Won ? 1000 : 0);
    }

    public int getPlayer2Score() {
        return roundNumbers * (!player1Won ? 1000 : 0);
    }

    public int getPlayer1Money() {
        return roundNumbers * (player1Won ? (1000 + player1MaxHp) : 100);
    }

    public int getPlayer2Money() {
        return roundNumbers * (!player1Won ? (1000 + player2MaxHp) : 100);
    }

    public boolean didPlayer1Won() {
        return player1Won;
    }
}
