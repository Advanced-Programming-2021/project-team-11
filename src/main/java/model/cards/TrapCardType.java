package model.cards;

import controller.GeneralUtil;

public enum TrapCardType {
    NORMAL,
    CONTINUES,
    QUICK_PLAY,
    FIELD,
    EQUIP,
    RITUAL;

    @Override
    public String toString() {
        return GeneralUtil.formatEnumName(super.toString());
    }
}
