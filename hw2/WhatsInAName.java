package hw2;

import java.util.Scanner;

public class WhatsInAName {
    
    public static void main(String[] args) {
        // Use a try-with-resource statement to create and auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user for their full name
            System.out.println("Please enter your first name and last name, separated by a space:");
            
            // Read the user's full name
            String fullName = scanner.nextLine();

            // Ensure the full name is actually provided
            if (fullName == null || fullName.isEmpty()) {
                System.out.println("Full name but provided");
                return;
            }

            // Ensure the full name contains a space
            if (!fullName.contains(" ")) {
                System.out.println("Full name must contain a space");
                return;
            }

            // Split the full name into first and last name
            String firstName = fullName.split(" ")[0];
            String lastName = fullName.split(" ")[1];

            // Print out information regarding the user's first and last names
            System.out.printf("Your first name is %s, which has %d characters%n", firstName, firstName.length());
            System.out.printf("Your last name is %s, which has %d characters%n", lastName, lastName.length());
            System.out.printf("Your initials are %s%s%n", firstName.charAt(0), lastName.charAt(0));
        }
    }

}
