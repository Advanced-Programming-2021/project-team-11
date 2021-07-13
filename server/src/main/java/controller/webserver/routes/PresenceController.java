package controller.webserver.routes;

import controller.webserver.TokenManager;
import io.javalin.http.Context;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import model.User;

import java.util.HashMap;
import java.util.Objects;

public class PresenceController {
    private static final HashMap<WsContext, User> onlineUsers = new HashMap<>();

    private static void registerWebsocket(WsContext ws, User user) {
        synchronized (onlineUsers) {
            onlineUsers.put(ws, user);
        }
    }

    public static long onlineUsers() {
        synchronized (onlineUsers) {
            return onlineUsers.values().stream().filter(Objects::nonNull).count();
        }
    }

    public static boolean isUserOnline(String username) {
        synchronized (onlineUsers) {
            return onlineUsers.values().stream().anyMatch(user -> user.getUsername().equals(username));
        }
    }

    private static void removeUser(WsContext ws) {
        synchronized (onlineUsers) {
            onlineUsers.remove(ws);
        }
        ScoreboardWebsockets.updateWebsockets();
    }

    public static void handleConnectWs(WsConnectContext wsConnectContext) {
        User user = TokenManager.getInstance().getUser(wsConnectContext.header(TokenManager.TOKEN_HEADER));
        if (user == null) {
            wsConnectContext.session.close();
            return;
        }
        registerWebsocket(wsConnectContext, user);
    }

    public static void handleCloseWs(WsCloseContext wsCloseContext) {
        removeUser(wsCloseContext);
    }

    public static void getActiveUsers(Context context) {
        context.result("" + onlineUsers());
    }
}
