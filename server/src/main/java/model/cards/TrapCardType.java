package model.cards;

import controller.GeneralUtil;

public enum TrapCardType {
    NORMAL,
    COUNTER,
    CONTINUES;

    @Override
    public String toString() {
        return GeneralUtil.formatEnumName(super.toString());
    }
}
