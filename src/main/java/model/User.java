package model;

import java.util.ArrayList;

public class User {
    private final static ArrayList<User> users = new ArrayList<>();
    private final String username;
    private String password, nickname;

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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
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
}
