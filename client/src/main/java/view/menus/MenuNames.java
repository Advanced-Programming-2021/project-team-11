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
    DUEL_SIDE_DECK_CHANGER("duel_side_deck_changer"),
    CHAT_ROOM("chat_room"),
    CARD_CREATOR("card_creator");

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
}
