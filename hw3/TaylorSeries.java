package hw3;

import java.util.Scanner;

public class TaylorSeries {
    
    public static void main(String[] args) {
        // Use a try-with-resource statement to create and auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user to enter a value for n
            System.out.print("Input n: ");
            String n = scanner.nextLine();

            // Prompt the user to enter a value for x
            System.out.print("Input x: ");
            String x = scanner.nextLine();

            // Ensure that the user's input is valid for both inputs
            if (!isInt(n) || !isDouble(x)) {
                System.out.println("Invalid Input!");
                return;
            }

            // Convert the user's input to numbers
            double paramN = Double.parseDouble(n);
            double paramX = Double.parseDouble(x);

            // Compute the Taylor series
            double taylor = 1;
            double term = 1;
            for (int i = 1; i <= paramN; i++) {
                term = (term * paramX) / i;
                taylor += term;
            }
            
            // Print the results
            System.out.printf("e^x is: %.2f\n", taylor);
        }
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
     * Attempt to parse the provided input using {@link Double#parseDouble(String)}.
     * If an exception is thrown, the input cannot be parsed as an double.
     * 
     * @param input The input to be parsed
     * @return true if the provided input can be parsed as an double, false otherwise
     */
    private static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
