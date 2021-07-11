package controller.menucontrollers;

import model.User;
import model.results.UserForScoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ScoreboardMenuController {
    public static List<UserForScoreboard> getScoreboardRows() {
        TreeSet<UserForScoreboard> users = new TreeSet<>();
        for (User user : User.getUsers())
            users.add(new UserForScoreboard(user));
        return sortWithRank(new ArrayList<>(users)).stream().limit(20).collect(Collectors.toList());
    }

    private static ArrayList<UserForScoreboard> sortWithRank(ArrayList<UserForScoreboard> scoreboard) {
        ArrayList<UserForScoreboard> result = new ArrayList<>(scoreboard.size());
        for (int i = 0, rankBefore = -1, scoreBefore = -1; i < scoreboard.size(); i++) {
            if (scoreBefore != scoreboard.get(i).getScore())
                scoreBefore = scoreboard.get(rankBefore = i).getScore();
            scoreboard.get(i).setRank(rankBefore + 1);
            result.add(scoreboard.get(i));
        }
        return result;
    }
}
