package model.database;

import com.google.gson.Gson;
import model.Deck;
import model.User;
import model.cards.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class UsersDatabase {
    private static Connection connection;

    /**
     * Opens an sqlite connection to database
     *
     * @param filename The database which is on disk
     */
    public static void connectToDatabase(String filename) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                "username VARCHAR(255) PRIMARY KEY," +
                "password TEXT NOT NULL," +
                "nickname TEXT NOT NULL," +
                "score INT NOT NULL DEFAULT 0," +
                "money INT NOT NULL DEFAULT 100000," +
                "active_deck VARCHAR(255) DEFAULT NULL," +
                "cards JSON NOT NULL DEFAULT (JSON_ARRAY())" +
                ")");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS decks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "owner VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "side_deck JSON NOT NULL DEFAULT (JSON_ARRAY())," +
                "main_deck JSON NOT NULL DEFAULT (JSON_ARRAY())," +
                "FOREIGN KEY (owner) REFERENCES users (username)" +
                ")");
        statement.close();
    }

    public static void loadUsers() throws SQLException {
        Statement statement = connection.createStatement();
        // Load users
        ResultSet rs = statement.executeQuery("select username, password, nickname, score, money, active_deck, cards from users");
        while (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            String nickname = rs.getString("nickname");
            int score = rs.getInt("score");
            int money = rs.getInt("money");
            String activeDeck = rs.getString("active_deck");
            String cardsArray = rs.getString("cards");
            registerUser(username, password, nickname, score, money, activeDeck, cardsArray);
        }
        rs.close();
        statement.close();
    }

    private static void registerUser(String username, String password, String nickname, int score, int money, String activeDeck, String cardsArray) throws SQLException {
        User user = new User(username, password, nickname);
        user.increaseScore(score);
        user.setMoney(money);
        user.setActiveDeck(activeDeck);
        String[] cards = new Gson().fromJson(cardsArray, String[].class);
        for (String cardName : cards) {
            Card card = Card.getCardByName(cardName);
            if (card != null)
                user.addCardToPlayer(card);
        }
        addDecksOfUser(user);
    }

    private static void addDecksOfUser(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, main_deck, side_deck FROM decks WHERE owner=?");
        preparedStatement.setString(1, user.getUsername());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            String[] mainCardsName = new Gson().fromJson(rs.getString("main_deck"), String[].class);
            String[] sideCardsName = new Gson().fromJson(rs.getString("side_deck"), String[].class);
            user.addDeck(name, createDeck(mainCardsName, sideCardsName));
        }
        rs.close();
    }

    private static Deck createDeck(String[] mainCardsName, String[] sideCardsName) {
        Deck deck = new Deck();
        for (String cardName : mainCardsName) {
            Card card = Card.getCardByName(cardName);
            if (card != null)
                deck.addCardToMainDeck(card);
        }
        for (String cardName : sideCardsName) {
            Card card = Card.getCardByName(cardName);
            if (card != null)
                deck.addCardToSideDeck(card);
        }
        return deck;
    }

    public static void saveUsers() throws SQLException {
        // Reset the database
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM decks");
        statement.executeUpdate("DELETE FROM users");
        statement.close();
        // Save each user
        for (User user : User.getUsers())
            saveUser(user);
    }

    private static void saveUser(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, password, nickname, score, money, active_deck, cards) VALUES (?,?,?,?,?,?,?)");
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getNickname());
        preparedStatement.setInt(4, user.getScore());
        preparedStatement.setInt(5, user.getMoney());
        preparedStatement.setString(6, user.getActiveDeckName());
        preparedStatement.setString(7, cardsToJsonArrayString(user.getAvailableCards()));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        for (Map.Entry<String, Deck> deck : user.getDecks().entrySet())
            saveDeck(user, deck.getKey(), deck.getValue());
    }

    private static void saveDeck(User owner, String name, Deck deck) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO decks (owner, name, side_deck, main_deck) VALUES (?,?,?,?)");
        preparedStatement.setString(1, owner.getUsername());
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, cardsToJsonArrayString(deck.getSideDeck()));
        preparedStatement.setString(4, cardsToJsonArrayString(deck.getMainDeck()));
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private static String cardsToJsonArrayString(ArrayList<Card> cards) {
        ArrayList<String> result = new ArrayList<>(cards.size());
        cards.forEach(card -> result.add(card.getName()));
        return new Gson().toJson(result.toArray(new String[0]));
    }

    public static void closeDatabase() throws SQLException {
        connection.close();
    }
}
