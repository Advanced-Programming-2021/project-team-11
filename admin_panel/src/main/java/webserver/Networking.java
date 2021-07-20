package webserver;

import exceptions.NetworkErrorException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import webserver.Types;

public class Networking {
    private static final String TOKEN_HEADER = "token";
    public static void sendIncreaseStock(String cardName, int delta, String token) throws NetworkErrorException {
        Types.ShopIncreaseStock body = new Types.ShopIncreaseStock();
        body.setCardName(cardName);
        body.setDelta(delta);
        HttpResponse<String> response = Unirest.post("/shop/increase_stock")
                .header(TOKEN_HEADER, token)
                .header("Content-Type", "application/json")
                .body(body)
                .asString();
        if (response.getStatus() != 200)
            throw new NetworkErrorException(response.getStatus(), response.getBody());
    }

    public static void changeForbidStatus(String cardName, boolean forbidden, String token) throws NetworkErrorException {
        Types.ShopChangeStatus body = new Types.ShopChangeStatus();
        body.setCardName(cardName);
        body.setForbidden(forbidden);
        HttpResponse<String> response = Unirest.post("/shop/forbid")
                .header(TOKEN_HEADER, token)
                .header("Content-Type", "application/json")
                .body(body)
                .asString();
        if (response.getStatus() != 200)
            throw new NetworkErrorException(response.getStatus(), response.getBody());
    }
}
