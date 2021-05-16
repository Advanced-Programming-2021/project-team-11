package model.enums;

public enum ActivateSpellCallback {
    RITUAL,
    NORMAL,
    /**
     * Done means that this had been handled in the game controller itself.
     * View doesn't have to do anything
     */
    DONE,
    EQUIP
}
