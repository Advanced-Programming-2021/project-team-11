package model.enums;

public enum GamePhase {
    DRAW("Draw"),
    STANDBY("Standby"),
    MAIN1("Main phase 1"),
    BATTLE_PHASE("Battle"),
    MAIN2("Main phase 2"),
    END_PHASE("End");

    private final String name;

    GamePhase(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
