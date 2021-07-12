package controller.webserver.routes;

import controller.menucontrollers.DeckMenuController;
import controller.webserver.TokenManager;
import controller.webserver.Types;
import controller.webserver.Webserver;
import io.javalin.http.Context;
import model.User;
import model.exceptions.*;

public class DeckRoute {
    public static void getDeck(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(Webserver.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        if (context.queryParamMap().containsKey("deck"))
            context.json(user.getDeckByName(context.queryParam("deck", "")));
        else
            context.json(user.getDecksForTable());
    }

    public static void createDeck(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(Webserver.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            DeckMenuController.addDeck(user, context.queryParam("name", ""));
        } catch (DeckExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void deleteDeck(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(Webserver.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            DeckMenuController.deleteDeck(user, context.queryParam("name", ""));
        } catch (DeckDoesNotExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void addCard(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(Webserver.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.DeckAddRemoveCard body = context.bodyAsClass(Types.DeckAddRemoveCard.class);
            DeckMenuController.addCardToDeck(user, body.getDeckName(), body.getCardName(), body.isSide());
        } catch (DeckDoesNotExistsException | DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void removeCard(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(Webserver.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.DeckAddRemoveCard body = context.bodyAsClass(Types.DeckAddRemoveCard.class);
            DeckMenuController.removeCardFromDeck(user, body.getDeckName(), body.getCardName(), body.isSide());
        } catch (DeckDoesNotExistsException | DeckCardNotExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void swapCard(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(Webserver.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.DeckSwapCard swapCard = context.bodyAsClass(Types.DeckSwapCard.class);
            DeckMenuController.swapCardBetweenDecks(user, swapCard.getDeckName(), swapCard.getCardName(), swapCard.isFromSide());
        } catch (DeckDoesNotExistsException | DeckCardNotExistsException | DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void setActive(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(Webserver.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            DeckMenuController.setActiveDeck(user, context.queryParam("name", ""));
        } catch (DeckDoesNotExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }
}
