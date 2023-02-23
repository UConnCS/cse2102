package hw2;

import java.util.Scanner;

public class BinaryToDecimal {

    public static void main(String[] args) {
        // Use a try-with-resource statement to create and auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt user for input, and then read the input as a string
            System.out.print("Please enter a 4 digit binary: ");
            String binary = scanner.nextLine();

            // Ensure the input is 4 digits long
            if (binary.length() != 4) {
                System.out.println("Binary input must be 4 digits long");
                return;
            }

            // Ensure the input only contains 0s and 1s using a Regex pattern
            if (!binary.matches("[01]+")) {
                System.out.println("Binary input must only contain 0s and 1s");
                return;
            }
            
            int result = 0;
            int multiplier = 8;

            // Loop through each bit, and apply the multiplier (8, 4, 2, 1) to it.
            for (int i = 0; i < binary.length(); i++) {
                int digit = binary.charAt(i) == '1' ? 1 : 0;
                result += digit * multiplier;
                multiplier /= 2;
            }

            // Print the final result
            System.out.println("The decimal value is " + result);
        }
    }

}