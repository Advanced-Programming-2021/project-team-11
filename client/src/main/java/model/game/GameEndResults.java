package model.game;

public class GameEndResults {
    private final boolean player1Won;
    private final int roundNumbers, player1MaxHp, player2MaxHp;

    public GameEndResults(int player1MaxHp, int player2MaxHp, boolean player1Won, int roundNumbers) {
        this.player1MaxHp = player1MaxHp;
        this.player2MaxHp = player2MaxHp;
        this.roundNumbers = roundNumbers;
        this.player1Won = player1Won;
    }

    public boolean didPlayer1Won() {
        return player1Won;
    }

    public int getPlayer1Score() {
        return player1Won ? 1000 * roundNumbers : 0;
    }

    public int getPlayer2Score() {
        return player1Won ? 0 : 1000 * roundNumbers;
    }

    public int getPlayer1Money() {
        return player1Won ? (1000 + player1MaxHp) * roundNumbers : 100 * roundNumbers;
    }

    public int getPlayer2Money() {
        return player1Won ? 100 * roundNumbers : (1000 + player2MaxHp) * roundNumbers;
    }
}
