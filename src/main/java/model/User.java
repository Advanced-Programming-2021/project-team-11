package model;

import java.util.ArrayList;

public class User {
    private final static ArrayList<User> users = new ArrayList<>();
    private final String username;
    private String password, nickname;
    private int score = 0, money = 0;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        users.add(this);
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int delta) {
        this.score += delta;
    }

    public static User getUserByUsername(String username) {
        for (User user : users)
            if (user.getUsername().equals(username))
                return user;
        return null;
    }

    public static User getUserByNickname(String nickname) {
        for (User user : users)
            if (user.getNickname().equals(nickname))
                return user;
        return null;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }
}
