package controller.menucontrollers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import model.UserForScoreboard;

import java.util.ArrayList;
import java.util.TreeSet;

public class ScoreboardMenuController {
    public static ObservableList<UserForScoreboard> getScoreboardRows() {
        TreeSet<UserForScoreboard> users = new TreeSet<>();
        for (User user : User.getUsers())
            users.add(new UserForScoreboard(user));
        return FXCollections.observableArrayList(sortWithRank(new ArrayList<>(users)));
    }

    private static ArrayList<UserForScoreboard> sortWithRank(ArrayList<UserForScoreboard> scoreboard) {
        ArrayList<UserForScoreboard> result = new ArrayList<>(scoreboard.size());
        int rankBefore = -1, scoreBefore = -1;
        for (int i = 0; i < scoreboard.size(); i++) {
            if (scoreBefore != scoreboard.get(i).getScore()) {
                rankBefore = i;
                scoreBefore = scoreboard.get(i).getScore();
            }
            scoreboard.get(i).setRank(rankBefore + 1);
            result.add(scoreboard.get(i));
        }
        return result;
    }
}
