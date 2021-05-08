package view;

import model.database.CardLoader;
import view.menus.LoginMenu;

public class Main {
    public static void main(String[] args) {
        CardLoader.loadCards("Monster.csv", "Spell.csv");
        new LoginMenu();
    }
}
