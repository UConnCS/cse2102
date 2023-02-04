package hw1;

public class VendingChange {
    
    public static void main(String[] args) {
        // Verify that the user provided a cents argument
        if (args.length != 1) {
            System.out.println("Missing cents argument.");
            System.exit(1);
        }

        // Verify that the provided argument can be parsed as an integer
        if (!isInt(args[0])) {
            System.out.println("Cents argument must be an integer.");
            System.exit(1);
        }

        // Parse args[0] as an integer, and compute the change that needs to be vended
        int cents = Integer.parseInt(args[0]);
        int change = 100 - cents;

        // Compute the number of quarters, dimes, and nickels that need to be vended
        int quarters = change / 25;
        change = change % 25;

        int dimes = change / 10;
        change = change % 10;

        int nickels = change / 5;
        change = change % 5;

        // Print the change that needs to be vended by coin
        System.out.printf(
            "You bought an item for %d and gave me a dollar, so your change is\n%d quarter%s, \n%d dime%s, and \n%d nickel%s.\n",
            cents, quarters, numberSuffix(quarters), dimes, numberSuffix(dimes), nickels, numberSuffix(nickels)
        );
    }

    /**
     * Attempt to parse the provided input using {@link Integer#parseInt(String)}.
     * If an exception is thrown, the input cannot be parsed as an integer.
     * 
     * @param input The input to be parsed
     * @return true if the provided input can be parsed as an integer, false otherwise
     */
    private static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the suffix string depending on the number.
     * 
     * @param input The input to be checked
     * @return an empty string if the provided input is 1, otherwise return "s"
     */
    private static String numberSuffix(int input) {
        if (input == 1)
            return "";
        return "s";
    }

}
