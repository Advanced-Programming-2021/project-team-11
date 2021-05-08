package view.menus;

import model.PlayerBoard;
import model.cards.monsters.ManEaterBug;

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
        position = DuelMenu.inputToRivalBoard(position);
        card.activateEffect(null, rivalBoard, rivalBoard.getMonsterCards()[position], 0);
    }
}
