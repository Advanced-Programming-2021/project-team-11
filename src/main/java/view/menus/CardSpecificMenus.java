package view.menus;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.CardType;
import model.cards.monsters.ManEaterBug;
import model.exceptions.CantActivateSpellException;

import java.util.ArrayList;

public class CardSpecificMenus {
    public static void handleManEaterBugRemoval(PlayerBoard rivalBoard, ManEaterBug card) {
        // At first check if the rival board is empty; Can't do anything if the board is empty!
        if (rivalBoard.isMonsterZoneEmpty())
            return;
        // Now ask the user to remove one card from rival
        System.out.print("Choose an position based on rival board to remove. Choose an empty position or type \"cancel\" to cancel. ");
        int position;
        while (true) {
            try {
                position = Integer.parseInt(MenuUtils.readLine());
                if (position >= 1 && position <= 5)
                    break;
            } catch (NumberFormatException ignored) {
            }
            System.out.println(MenuUtils.INVALID_NUMBER);
        }
        position = DuelMenuUtils.inputToRivalBoard(position);
        card.activateEffect(null, rivalBoard, null, rivalBoard.getMonsterCards()[position], 0);
    }

    public static void handleScannerCardEffect(ArrayList<PlayableCard> rivalGraveyard, PlayableCard scannerCard) throws CantActivateSpellException {
        if (rivalGraveyard.stream().noneMatch(card -> card.getCard().getCardType() == CardType.MONSTER))
            throw new CantActivateSpellException();
        // Show the graveyard
        DuelMenuUtils.printGraveyard(rivalGraveyard, "rival");
        // Get the card
        int index = -1;
        while (index < 0 || index >= rivalGraveyard.size()) {
            System.out.print("Choose a card by it's number: ");
            String command = MenuUtils.readLine();
            if (command.equals(MenuUtils.CANCEL_COMMAND))
                return;
            try {
                index = Integer.parseInt(MenuUtils.readLine());
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
            if (rivalGraveyard.get(index).getCard().getCardType() != CardType.MONSTER) {
                index = -1;
                System.out.println("Please select a monster card");
            }
        }
        scannerCard.activateEffect(null, null, rivalGraveyard.get(index));
        System.out.printf("Your scanner is now %s!\n", rivalGraveyard.get(index).getCard().getName());
    }
}
