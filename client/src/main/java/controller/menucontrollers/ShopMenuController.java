package controller.menucontrollers;

import controller.webserver.GsonHelper;
import controller.webserver.Types;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import model.cards.CardShopDetails;
import model.exceptions.NetworkErrorException;

import java.util.HashMap;

public class ShopMenuController {
    public static void buyCard(String cardName) throws NetworkErrorException {
        Types.ShopCard card = new Types.ShopCard();
        card.setCard(cardName);
        HttpResponse<String> response = Unirest.post("/shop/buy")
                .header(LoginMenuController.TOKEN_HEADER, LoginMenuController.getToken())
                .header("Content-Type", "application/json")
                .body(card)
                .asString();
        if (response.getStatus() != 200)
            throw new NetworkErrorException(response);
    }

    public static void sellCard(String cardName) throws NetworkErrorException {
        Types.ShopCard card = new Types.ShopCard();
        card.setCard(cardName);
        HttpResponse<String> response = Unirest.post("/shop/sell")
                .header(LoginMenuController.TOKEN_HEADER, LoginMenuController.getToken())
                .header("Content-Type", "application/json")
                .body(card)
                .asString();
        if (response.getStatus() != 200)
            throw new NetworkErrorException(response);
    }

    public static HashMap<String, CardShopDetails> getAllCards() {
        HttpResponse<String> response = Unirest.get("/shop/cards")
                .asString();
        return GsonHelper.fromMap(response.getBody(), String.class, CardShopDetails.class);
    }
}
