package controller.webserver;

import controller.webserver.chat.ChatController;
import controller.webserver.routes.*;
import io.javalin.Javalin;
import model.database.CardLoader;
import model.database.UsersDatabase;

import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Webserver {
    private static String adminToken;

    public static void startWebserver(int port, String adminToken) {
        Webserver.adminToken = adminToken;
        Javalin app = Javalin.create(config -> config.asyncRequestTimeout = 10_000L).routes(() -> {
            path("users", () -> {
                post("register", UsersRoute::register);
                ws("scoreboard", ScoreboardWebsockets::registerWebsocket);
                post("login", UsersRoute::login);
                delete("logout", UsersRoute::logout);
                path("profile", () -> {
                    post("nickname", UsersRoute::updateProfileNickname);
                    post("password", UsersRoute::updateProfilePassword);
                    post("image", UsersRoute::updateProfileImage);
                    get(UsersRoute::getProfile);
                });
                path("presence", () -> {
                    get(PresenceController::getActiveUsers);
                    ws(websocket -> {
                        websocket.onConnect(PresenceController::handleConnectWs);
                        websocket.onClose(PresenceController::handleCloseWs);
                    });
                });
            });
            ws("chat", ChatController::handleWebsocket);
            path("shop", () -> {
                get("cards", ShopRoute::getCards);
                post("buy", ShopRoute::buyCard);
                post("sell", ShopRoute::sellCard);
                post("increase_stock", ShopRoute::increaseStock);
                post("forbid", ShopRoute::changeForbidStatus);
            });
            path("deck", () -> {
                get(DeckRoute::getDeck);
                put(DeckRoute::createDeck);
                delete(DeckRoute::deleteDeck);
                post("add-card", DeckRoute::addCard);
                post("remove-card", DeckRoute::removeCard);
                post("swap-card", DeckRoute::swapCard);
                put("set-active", DeckRoute::setActive);
            });
        }).start(port);
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
        app.events(event -> event.serverStopping(Webserver::saver));
    }

    private static void saver() {
        try {
            CardLoader.saveCards();
            UsersDatabase.saveUsers();
            UsersDatabase.closeDatabase();
        } catch (SQLException ex) {
            System.out.println("Cannot save the database: " + ex.getMessage());
        }
    }

    public static String getAdminToken() {
        return adminToken;
    }
}
