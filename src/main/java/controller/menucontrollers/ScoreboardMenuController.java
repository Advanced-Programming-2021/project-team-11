package controller.menucontrollers;

import model.User;
import model.UserForScoreboard;

import java.util.ArrayList;
import java.util.TreeSet;

public class ScoreboardMenuController {
    public static ArrayList<String> getScoreboardLines() {
        TreeSet<UserForScoreboard> users = new TreeSet<>();
        for (User user : User.getUsers())
            users.add(new UserForScoreboard(user));
        return sortWithRank(new ArrayList<>(users));
    }

    private static ArrayList<String> sortWithRank(ArrayList<UserForScoreboard> scoreboard) {
        ArrayList<String> result = new ArrayList<>(scoreboard.size());
        int rankBefore = -1, scoreBefore = -1;
        for (int i = 0; i < scoreboard.size(); i++) {
            if (scoreBefore != scoreboard.get(i).getScore()) {
                rankBefore = i;
                scoreBefore = scoreboard.get(i).getScore();
            }
            result.add(scoreboard.get(i).formatWithRank(rankBefore + 1));
        }
        return result;
    }
}
