package view.menus;

enum MenuNames {
    LOGIN,
    MAIN,
    DUEL,
    DECK,
    SCOREBOARD,
    PROFILE,
    SHOP,
    IMPORT_EXPORT,
    INVALID;

    /**
     * Parses {@link MenuNames} from menu
     *
     * @param name The name to parse
     * @return The menu enum. If the name is invalid, returns {@link #INVALID}
     */
    static MenuNames parseMenuName(String name) {
        switch (name) {
            case "Login":
                return LOGIN;
            case "Main":
                return MAIN;
            case "Duel":
                return DUEL;
            case "Deck":
                return DECK;
            case "Scoreboard":
                return SCOREBOARD;
            case "Profile":
                return PROFILE;
            case "Shop":
                return SHOP;
            case "Import/Export":
                return IMPORT_EXPORT;
            default:
                return INVALID;
        }
    }
}
