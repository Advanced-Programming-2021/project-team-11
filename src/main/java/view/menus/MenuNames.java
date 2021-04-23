package view.menus;

public enum MenuNames {
    LOGIN,
    MAIN,
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
            default:
                return INVALID;
        }
    }
}
