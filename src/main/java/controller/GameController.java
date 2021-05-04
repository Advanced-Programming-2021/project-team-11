package controller;

import model.Player;
import model.PlayerBoard;
import model.User;
import model.enums.GameRounds;
import model.game.GameEndResults;

import java.util.ArrayList;

public class GameController {
    private final User player1, player2;
    private int player1RoundsWon, player2RoundsWon;
    private int player1MaxHealth, player2MaxHealth;
    private GameRoundController round;
    private final GameRounds rounds;
    /**
     * On each round we swap this
     */
    private boolean player1Starting = true;

    public GameController(User player1, User player2, GameRounds rounds) {
        this.rounds = rounds;
        this.player1 = player1;
        this.player2 = player2;
        setupRound();
    }

    public GameRoundController getRound() {
        return round;
    }

    /**
     * Checks if the round has been ended and returns the round result
     *
     * @return null if the round is still not done. Otherwise the round info
     */
    public GameEndResults isGameEnded() {
        if (rounds == GameRounds.ONE) {
            if (player1RoundsWon == 1)
                return new GameEndResults(player1MaxHealth, player2MaxHealth, true, 1);
            else if (player2RoundsWon == 1)
                return new GameEndResults(player1MaxHealth, player2MaxHealth, false, 1);
            return null;
        }
        if (player1RoundsWon == 2)
            return new GameEndResults(player1MaxHealth, player2MaxHealth, true, 3);
        else if (player2RoundsWon == 2)
            return new GameEndResults(player1MaxHealth, player2MaxHealth, false, 3);
        return null;
    }

    public void setupRound() {
        PlayerBoard player1Board = new PlayerBoard(new Player(player1), new ArrayList<>(player1.getActiveDeck().getMainDeck()));
        player1Board.shuffleDeck();
        PlayerBoard player2Board = new PlayerBoard(new Player(player2), new ArrayList<>(player2.getActiveDeck().getMainDeck()));
        player2Board.shuffleDeck();
        round = new GameRoundController(player1Board, player2Board, player1Starting);
    }
}
