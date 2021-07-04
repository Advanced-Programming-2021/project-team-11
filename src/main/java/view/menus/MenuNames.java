package view.menus;

enum MenuNames {
    ROOT("root"),
    LOGIN("login"),
    REGISTER("register"),
    MAIN("main"),
    DUEL("duel"),
    DUEL_START("duel_start"),
    DECK("deck"),
    DECK_DETAILS("deck_details"),
    SCOREBOARD("scoreboard"),
    PROFILE("profile"),
    SHOP("shop"),
    IMPORT_EXPORT("import_export"),
    INVALID("");

    private final String fxmlName;

    MenuNames(String fxmlName) {
        this.fxmlName = "/view/" + fxmlName + "_menu.fxml";
    }

    /**
     * @return Fxml resource path name of this menu
     */
    public String getFxmlPath() {
        return fxmlName;
    }

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
            case "Duel Start":
                return DUEL_START;
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
