package controller.menucontrollers;

import model.Deck;
import model.User;
import model.cards.Card;
import model.exceptions.*;
import model.results.GetDecksResult;

import java.util.*;

public class DeckMenuController {
    public static void addDeck(User user, String deckName) throws DeckExistsException {
        if (user.getDeckByName(deckName) != null)
            throw new DeckExistsException(deckName);
        user.addDeck(deckName);
    }

    public static void deleteDeck(User user, String deckName) throws DeckDoesNotExistsException {
        Deck deck = user.getDeckByName(deckName);
        if (deck == null)
            throw new DeckDoesNotExistsException(deckName);
        if (user.getActiveDeckName().equals(deckName)) // de-activate the active deck if needed
            user.setActiveDeck(null);
        // Return cards to user
        deck.getMainDeck().forEach(user::addCardToPlayer);
        deck.getSideDeck().forEach(user::addCardToPlayer);
        user.deleteDeck(deckName);
    }

    public static void setActiveDeck(User user, String deckName) throws DeckDoesNotExistsException {
        if (user.getDeckByName(deckName) == null)
            throw new DeckDoesNotExistsException(deckName);
        user.setActiveDeck(deckName);
    }

    public static void addCardToDeck(User user, String deckName, String cardName, boolean isSide) throws CardNotExistsException, DeckDoesNotExistsException, DeckSideOrMainFullException, DeckHaveThreeCardsException {
        Optional<Card> cardChecker = user.getAvailableCards().stream().filter(x -> x.getName().equals(cardName)).findFirst();
        if (!cardChecker.isPresent())
            throw new CardNotExistsException(cardName);
        Card card = cardChecker.get();
        Deck deck = user.getDeckByName(deckName);
        if (deck == null)
            throw new DeckDoesNotExistsException(deckName);
        if (isSide ? deck.isSideFull() : deck.isMainFull())
            throw new DeckSideOrMainFullException(isSide);
        if (deck.haveThreeCards(card))
            throw new DeckHaveThreeCardsException(card, deckName);
        // Process the card
        if (isSide)
            deck.addCardToSideDeck(card);
        else
            deck.addCardToMainDeck(card);
        user.removeCardFromPlayer(card);
    }

    public static void removeCardFromDeck(User user, String deckName, String cardName, boolean isSide) throws DeckDoesNotExistsException, DeckCardNotExistsException {
        Deck deck = user.getDeckByName(deckName);
        if (deck == null)
            throw new DeckDoesNotExistsException(deckName);
        Optional<Card> cardChecker = isSide ? deck.getSideDeck().stream().filter(x -> x.getName().equals(cardName)).findFirst() : deck.getMainDeck().stream().filter(x -> x.getName().equals(cardName)).findFirst();
        if (!cardChecker.isPresent())
            throw new DeckCardNotExistsException(cardName, isSide);
        // Process the card
        Card card = cardChecker.get();
        if (isSide)
            deck.removeCardFromSideDeck(card);
        else
            deck.removeCardFromMainDeck(card);
        user.addCardToPlayer(card);
    }

    public static GetDecksResult getDecks(User user) {
        String activeDeckName = user.getActiveDeckName();
        TreeSet<GetDecksResult.DeckResult> otherDecks = new TreeSet<>();
        for (Map.Entry<String, Deck> entry : user.getDecks().entrySet())
            if (!entry.getKey().equals(activeDeckName))
                otherDecks.add(new GetDecksResult.DeckResult(entry.getKey(), entry.getValue()));
        return new GetDecksResult(activeDeckName == null ? null : new GetDecksResult.DeckResult(activeDeckName, user.getActiveDeck()), otherDecks);
    }

    public static ArrayList<Card> getDeckCards(User user, String deckName, boolean isSide) throws DeckDoesNotExistsException {
        Deck deck = user.getDeckByName(deckName);
        if (deck == null)
            throw new DeckDoesNotExistsException(deckName);
        return isSide ? deck.getSideDeck() : deck.getMainDeck();
    }

    public static ArrayList<Card> getAllCards(User user) {
        TreeSet<Card> cards = new TreeSet<>(user.getAvailableCards());
        for (Deck deck : user.getDecks().values()) {
            cards.addAll(deck.getMainDeck());
            cards.addAll(deck.getSideDeck());
        }
        return new ArrayList<>(cards);
    }

    public static void swapCards(Deck deck, String mainDeckCardName, String sideDeckCardName) throws DeckCardNotExistsException {
        // Get cards
        Optional<Card> mainDeckCardCandidate = deck.getMainDeck().stream().filter(x -> x.getName().equals(mainDeckCardName)).findFirst();
        if (!mainDeckCardCandidate.isPresent())
            throw new DeckCardNotExistsException(mainDeckCardName, false);
        Optional<Card> sideDeckCardCandidate = deck.getSideDeck().stream().filter(x -> x.getName().equals(sideDeckCardName)).findFirst();
        if (!sideDeckCardCandidate.isPresent())
            throw new DeckCardNotExistsException(sideDeckCardName, true);
        // Swap!
        deck.removeCardFromMainDeck(mainDeckCardCandidate.get());
        deck.removeCardFromSideDeck(sideDeckCardCandidate.get());
        deck.addCardToMainDeck(sideDeckCardCandidate.get());
        deck.addCardToSideDeck(mainDeckCardCandidate.get());
    }

    public static String addAllCardsToNewDeck(User user) {
        final String deckName = "deckup-deck";
        try {
            addDeck(user, deckName);
        } catch (Exception ex) {
        }
        ArrayList<Card> cards = new ArrayList<>(Card.getAllCards());
        Collections.shuffle(cards);
        for (Card card : cards)
            try {
                user.addCardToPlayer(card);
                addCardToDeck(user, deckName, card.getName(), false);
            } catch (Exception ignored) {
                try {
                    addCardToDeck(user, deckName, card.getName(), true);
                } catch (Exception ignored2) {
                }
            }
        return deckName;
    }
}
