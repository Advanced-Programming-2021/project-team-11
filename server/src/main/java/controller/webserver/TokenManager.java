package controller.webserver;

import model.User;

import java.util.HashMap;
import java.util.UUID;

public class TokenManager {
    public final static String TOKEN_HEADER = "token";
    private final static TokenManager tokenManager = new TokenManager();
    private final HashMap<String, User> users = new HashMap<>();

    private TokenManager() {

    }

    public static TokenManager getInstance() {
        return tokenManager;
    }

    public String addUser(User user) {
        String token = UUID.randomUUID().toString();
        synchronized (users) {
            users.put(token, user);
        }
        return token;
    }

    public void logoutUser(String token) {
        synchronized (users) {
            users.remove(token);
        }
    }

    public User getUser(String token) {
        synchronized (users) {
            return users.get(token);
        }
    }
}
