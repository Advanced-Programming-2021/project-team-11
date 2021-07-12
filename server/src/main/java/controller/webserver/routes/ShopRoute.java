package controller.webserver.routes;

import controller.menucontrollers.ShopMenuController;
import controller.webserver.TokenManager;
import controller.webserver.Types;
import controller.webserver.Webserver;
import io.javalin.http.Context;
import model.User;
import model.database.UsersDatabase;
import model.exceptions.CardNotExistsException;
import model.exceptions.ForbiddenCardException;
import model.exceptions.InsufficientBalanceException;
import model.exceptions.OutOfStockException;

import java.sql.SQLException;
import java.util.Objects;

public class ShopRoute {
    public static void getCards(Context context) {
        context.json(UsersDatabase.getCardStocks());
    }

    public static void increaseStock(Context context) {
        if (!Objects.equals(context.header(TokenManager.TOKEN_HEADER), Webserver.getAdminToken())) {
            context.status(401);
            return;
        }
        Types.ShopIncreaseStock body = context.bodyAsClass(Types.ShopIncreaseStock.class);
        try {
            UsersDatabase.increaseCardStock(body.getCardName(), body.getDelta());
        } catch (SQLException ex) {
            ex.printStackTrace();
            context.status(500);
        }
    }

    public static void changeForbidStatus(Context context) {
        if (!Objects.equals(context.header(TokenManager.TOKEN_HEADER), Webserver.getAdminToken())) {
            context.status(401);
            return;
        }
        Types.ShopChangeStatus body = context.bodyAsClass(Types.ShopChangeStatus.class);
        try {
            UsersDatabase.changeCardStatus(body.getCardName(), body.isForbidden());
        } catch (SQLException ex) {
            ex.printStackTrace();
            context.status(500);
        }
    }

    public static void buyCard(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(TokenManager.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.ShopCard body = context.bodyAsClass(Types.ShopCard.class);
            UsersDatabase.tryDecreaseCardStock(body.getCard());
            ShopMenuController.buyCardForUser(user, body.getCard());
        } catch (InsufficientBalanceException | CardNotExistsException | OutOfStockException | ForbiddenCardException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        } catch (SQLException ex) {
            ex.printStackTrace();
            context.status(500);
        }
    }

    public static void sellCard(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(TokenManager.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.ShopCard body = context.bodyAsClass(Types.ShopCard.class);
            ShopMenuController.sellCardForUser(user, body.getCard());
            UsersDatabase.tryIncreaseCardStock(body.getCard());
        } catch (CardNotExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
