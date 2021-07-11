package controller.webserver.routes;

import controller.webserver.Types;
import controller.webserver.Webserver;
import io.javalin.http.Context;
import model.database.UsersDatabase;

import java.util.Objects;

public class ShopRoute {
    public static void getCards(Context context) {
        context.json(UsersDatabase.getCardStocks());
    }

    public static void increaseStock(Context context) {
        if (!Objects.equals(context.header(Webserver.TOKEN_HEADER), Webserver.getAdminToken())) {
            context.status(401);
            return;
        }
        Types.ShopIncreaseStockRequest body = context.bodyAsClass(Types.ShopIncreaseStockRequest.class);
        UsersDatabase.increaseCardStock(body.getCardName(), body.getDelta());
    }

    public static void changeForbidStatus(Context context) {
        if (!Objects.equals(context.header(Webserver.TOKEN_HEADER), Webserver.getAdminToken())) {
            context.status(401);
            return;
        }
        Types.ShopChangeStatusRequest body = context.bodyAsClass(Types.ShopChangeStatusRequest.class);
        UsersDatabase.changeCardStatus(body.getCardName(), body.isForbidden());
    }
}
