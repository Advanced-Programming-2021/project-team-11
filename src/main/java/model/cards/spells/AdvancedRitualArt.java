package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterCard;
import model.cards.RitualMonster;
import model.cards.SpellCard;
import model.cards.SpellCardType;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class AdvancedRitualArt extends SpellCard {
    private final static String CARD_NAME = "Advanced Ritual Art";
    private static AdvancedRitualArt instance;

    private AdvancedRitualArt() {
        super(CARD_NAME, SpellCardType.RITUAL);
    }

    public static AdvancedRitualArt getInstance() {
        if (instance == null)
            instance = new AdvancedRitualArt();
        return instance;
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    /**
     * Checks if user have at least one {@link RitualMonster} card
     *
     * @param board The player board
     * @return True if there is at least one {@link RitualMonster} in user hand
     */
    public static boolean isRitualSummonPossible(PlayerBoard board) {
        TreeSet<Integer> levelSums = subsetSum(board.getMonsterCardsList().stream().mapToInt(card -> ((MonsterCard) card.getCard()).getLevel()));
        return board.getHand().stream().anyMatch(card -> card.getCard() instanceof RitualMonster &&
                levelSums.contains(((RitualMonster) card.getCard()).getLevel()));
    }

    private static TreeSet<Integer> subsetSum(IntStream stream) {
        ArrayList<Integer> numbers = new ArrayList<>();
        TreeSet<Integer> result = new TreeSet<>();
        stream.forEach(numbers::add);
        int total = 1 << numbers.size();
        // https://www.geeksforgeeks.org/print-sums-subsets-given-set/
        for (int i = 0; i < total; i++) {
            int sum = 0;
            for (int j = 0; j < numbers.size(); j++)
                if ((i & (1 << j)) != 0)
                    sum += numbers.get(j);
            result.add(sum);
        }
        return result;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    /**
     * Checks if there is a way for player to tribute the cards needed to summon this card
     *
     * @param myBoard           Player board
     * @param rivalBoard        No effect
     * @param thisCard          The card which we want to summon
     * @param activationCounter No effect
     * @return True if the monster can be summoned. Otherwise false
     */
    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        TreeSet<Integer> levelSums = subsetSum(myBoard.getMonsterCardsList().stream().mapToInt(card -> ((MonsterCard) card.getCard()).getLevel()));
        return levelSums.contains(((RitualMonster) thisCard.getCard()).getLevel());
    }
}
