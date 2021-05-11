package model.cards;

import controller.GeneralUtil;

public enum SpellCardType {
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
