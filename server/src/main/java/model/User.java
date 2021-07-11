package model;

import com.talanlabs.avatargenerator.GitHubAvatar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.cards.Card;
import model.exceptions.UserDeckIsInvalidException;
import model.exceptions.UserHaveNoActiveDeckException;
import model.results.DeckListTableResult;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class User {
    private final static Color[] profilePicColors = {Color.BLACK, Color.MAGENTA, Color.PINK, Color.GREEN, Color.red};
    private final static ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Card> availableCards = new ArrayList<>();
    private final HashMap<String, Deck> decks = new HashMap<>();
    private final String username;
    /**
     * The name of active deck
     */
    private String activeDeck = null;
    private String password, nickname;
    private byte[] profilePic = null;
    private int score = 0, money = 100000;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        synchronized (users) {
            users.add(this);
        }
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

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public void setMoney(int money) {
        this.money = money;
    }

    public void decreaseMoney(int delta) {
        this.money -= delta;
    }

    public void increaseMoney(int delta) {
        this.money += delta;
    }

    public void addDeck(String deckName) {
        decks.put(deckName, new Deck());
    }

    public void addDeck(String deckName, Deck deck) {
        decks.put(deckName, deck);
    }

    public void deleteDeck(String deckName) {
        decks.remove(deckName);
    }

    public HashMap<String, Deck> getDecks() {
        return decks;
    }

    public ObservableList<DeckListTableResult> getDecksForTable() {
        ArrayList<DeckListTableResult> result = new ArrayList<>();
        getDecks().forEach((deckName, deck) -> result.add(new DeckListTableResult(deckName, deck, deckName.equals(activeDeck))));
        result.sort(Comparator.comparing(DeckListTableResult::getName));
        return FXCollections.observableArrayList(result);
    }

    public String getActiveDeckName() {
        return this.activeDeck;
    }

    public Deck getActiveDeck() {
        return getActiveDeckName() != null ? getDeckByName(getActiveDeckName()) : null;
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

    public byte[] getProfilePicBytes() {
        return this.profilePic;
    }

    private ByteArrayInputStream getProfilePicByteStream() {
        if (getProfilePicBytes() == null) {
            long hash = getUsername().hashCode() + 100000; // some problems with library. It doesn't accept numbers less than 100000
            Color picColor = profilePicColors[new Random(hash).nextInt(profilePicColors.length)];
            return new ByteArrayInputStream(GitHubAvatar.newAvatarBuilder().color(picColor).build().createAsPngBytes(hash));
        } else
            return new ByteArrayInputStream(getProfilePicBytes());
    }

    public void setProfilePicBytes(byte[] bytes) {
        this.profilePic = bytes;
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

    public void validateUserActiveDeck() throws UserHaveNoActiveDeckException, UserDeckIsInvalidException {
        Deck deck = getActiveDeck();
        if (deck == null)
            throw new UserHaveNoActiveDeckException(this);
        if (!deck.isValid())
            throw new UserDeckIsInvalidException(this);
    }
}
