package controller.webserver.routes;

import com.google.gson.Gson;
import controller.menucontrollers.ScoreboardMenuController;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsHandler;

import java.util.HashSet;

public class ScoreboardWebsockets {
    private static final Gson gson = new Gson();
    private static final HashSet<WsContext> websockets = new HashSet<>();

    public static void updateWebsockets() {
        synchronized (websockets) {
            websockets.forEach(ws -> ws.send(gson.toJson(ScoreboardMenuController.getScoreboardRows())));
        }
    }

    public static void registerWebsocket(WsHandler ws) {
        ws.onConnect(ctx -> {
            synchronized (websockets) {
                websockets.add(ctx);
            }
            ctx.send(gson.toJson(ScoreboardMenuController.getScoreboardRows()));
        });
    }
}
