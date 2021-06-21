package model;

import com.talanlabs.avatargenerator.GitHubAvatar;
import javafx.scene.image.Image;
import model.cards.Card;
import model.exceptions.UserDeckIsInvalidException;
import model.exceptions.UserHaveNoActiveDeckException;
import view.menus.MainMenu;

import java.awt.*;
import java.io.ByteArrayInputStream;
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
    private byte[] profilePic = null;
    private int score = 0, money = 100000;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        users.add(this);
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

    public String getActiveDeckName() {
        return this.activeDeck;
    }

    public Deck getActiveDeck() {
        if (getActiveDeckName() == null)
            return null;
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

    public byte[] getProfilePicBytes() {
        return this.profilePic;
    }

    private ByteArrayInputStream getProfilePicByteStream() {
        if (getProfilePicBytes() == null) {
            long hash = MainMenu.loggedInUser.getNickname().hashCode() + 100000; // some problems with library. It doesn't accept numbers less than 100000
            return new ByteArrayInputStream(GitHubAvatar.newAvatarBuilder().color(Color.PINK).build().createAsPngBytes(hash));
        } else {
            return new ByteArrayInputStream(getProfilePicBytes());
        }
    }

    public Image getProfilePicImage() {
        return new Image(getProfilePicByteStream());
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
