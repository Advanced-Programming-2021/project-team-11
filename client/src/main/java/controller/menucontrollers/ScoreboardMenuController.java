package controller.menucontrollers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import model.results.UserForScoreboard;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ScoreboardMenuController {
    public static ObservableList<UserForScoreboard> getScoreboardRows() {
        return null;
        /*TreeSet<UserForScoreboard> users = new TreeSet<>();
        for (User user : User.getUsers())
            users.add(new UserForScoreboard(user));
        return FXCollections.observableArrayList(sortWithRank(new ArrayList<>(users)).stream().limit(20).collect(Collectors.toList()));
    */
    }
}
