package view.components;

import model.cards.Card;

public interface CardHoverCallback {
    void cardHovered(Card card, boolean shouldShow);
}
