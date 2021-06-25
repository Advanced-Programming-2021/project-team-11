package model.cards;

import controller.GeneralUtil;

public enum SpellCardType {
    QUICK_PLAY,
    CONTINUES,
    NORMAL,
    RITUAL,
    FIELD,
    EQUIP;

    @Override
    public String toString() {
        return GeneralUtil.formatEnumName(super.toString());
    }
}
