package view.menus;

import model.User;
import model.enums.GameRounds;
import model.exceptions.InvalidCommandException;

public class DuelMenu extends Menu {
    private static final String SUMMON_COMMAND = "summon", FLIP_SUMMON_COMMAND = "flip-summon",
            ATTACK_PREFIX_COMMAND = "attack ", ATTACK_DIRECT_COMMAND = "attack direct",
            ACTIVATE_EFFECT_COMMAND = "activate effect", SHOW_CARD_COMMAND = "card show --selected",
            SURRENDER_COMMAND = "surrender", CHEAT_HP = "PAINKILLER";
    private final User player1, player2;

    DuelMenu(User player1, User player2, GameRounds rounds) {
        this.player1 = player1;
        this.player2 = player2;
        // Say who is the beginner
        System.out.printf("%s is the beginner!", player1.getUsername());
    }

    @Override
    void openMenu() {
        while (true) {
            String command = MenuUtils.readLine();
            try {
                if (processMenuCommands(command))
                    System.out.println(MenuUtils.MENU_NAV_FAILED); // THERE IS NO WAY OUT!!!!
                continue;
            } catch (InvalidCommandException ignored) {
            }
            // Check commands
        }
    }

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Duel Menu");
    }
}
