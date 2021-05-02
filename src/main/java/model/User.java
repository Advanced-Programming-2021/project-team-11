package model;

import model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private final static ArrayList<User> users = new ArrayList<>();
    private final String username;
    private final HashMap<String, Deck> decks = new HashMap<>();
    private final ArrayList<Card> availableCards = new ArrayList<>();
    /**
     * The name of active deck
     */
    private String activeDeck = null;
    private String password, nickname;
    private int score = 0, money = 0;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        users.add(this);
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public int getScore() {
        return this.score;
    }

    public int getMoney() {
        return this.money;
    }

    public void addDeck(String deckName) {
        decks.put(deckName, new Deck());
    }

    public void deleteDeck(String deckName) {
        decks.remove(deckName);
    }

    public HashMap<String, Deck> getDecks() {
        return decks;
    }

    public String getActiveDeckName() {
        return this.activeDeck;
    }

    public Deck getActiveDeck() {
        return getDeckByName(getActiveDeckName());
    }

    public void setActiveDeck(String deckName) {
        this.activeDeck = deckName;
    }

    public Deck getDeckByName(String deckName) {
        return decks.get(deckName);
    }

    public ArrayList<Card> getAvailableCards() {
        return availableCards;
    }

    public void removeCardFromPlayer(Card card) {
        availableCards.remove(card);
    }

    public void addCardToPlayer(Card card) {
        availableCards.add(card);
    }

    public void increaseScore(int delta) {
        this.score += delta;
    }

    public static User getUserByUsername(String username) {
        for (User user : users)
            if (user.getUsername().equals(username))
                return user;
        return null;
    }

    public static User getUserByNickname(String nickname) {
        for (User user : users)
            if (user.getNickname().equals(nickname))
                return user;
        return null;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }
}
