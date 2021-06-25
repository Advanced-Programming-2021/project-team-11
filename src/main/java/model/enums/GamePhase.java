package model.enums;

public enum GamePhase {
    BATTLE_PHASE("Battle"),
    MAIN1("Main phase 1"),
    MAIN2("Main phase 2"),
    STANDBY("Standby"),
    END_PHASE("End"),
    DRAW("Draw");

    private final String name;

    GamePhase(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
