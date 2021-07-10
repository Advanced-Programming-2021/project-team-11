package model.cards;

import controller.GeneralUtil;

public enum CardType {
    MONSTER,
    SPELL,
    TRAP;

    @Override
    public String toString() {
        return GeneralUtil.formatEnumName(super.toString());
    }
}
