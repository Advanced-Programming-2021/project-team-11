import exceptions.NetworkErrorException;
import webserver.Networking;

public class ApplicationManager {
    private static String token;

    public static void run(String token) {
        ApplicationManager.token = token;
        System.out.println("Welcome to admin panel!");
        while (true) {
            System.out.println("Choose one option:");
            System.out.println("1. Increase card stock");
            System.out.println("2. Forbid/Allow card");
            System.out.println("3. Exit");
            try {
                int number = Integer.parseInt(Util.readLine());
                switch (number) {
                    case 1:
                        handleIncreaseStock();
                        break;
                    case 2:
                        handleForbidCard();
                        break;
                    case 3:
                        System.out.println("Bye!");
                        return;
                    default:
                        throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number");
            }
        }
    }

    private static void handleForbidCard() {
        System.out.print("Which card you want to change it's status? ");
        String cardName = Util.readLine();
        System.out.println("Make this card forbidden? (y/n) ");
        boolean forbidden = Util.readLine().equals("y");
        try {
            Networking.changeForbidStatus(cardName, forbidden, token);
            System.out.println("Done!");
        } catch (NetworkErrorException e) {
            System.out.println("Cannot change status: " + e.getMessage());
        }
    }

    private static void handleIncreaseStock() {
        System.out.print("Which card you want to increase it's stock? ");
        String cardName = Util.readLine();
        System.out.print("How much you want to increase it's stock? ");
        int amount = Integer.parseInt(Util.readLine());
        try {
            Networking.sendIncreaseStock(cardName, amount, token);
            System.out.println("Done!");
        } catch (NetworkErrorException e) {
            System.out.println("Cannot increase stock: " + e.getMessage());
        }
    }
}
