package hw2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class ReadALine {
    
    public static void main(String[] args) {
        // Use a try-with-resource statement to create and auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt user for input
            System.out.println("Enter a line of text. No punctuation please.");
            
            // Read the line of text, then split it into individual words
            LinkedList<String> words = new LinkedList<>(Arrays.asList(scanner.nextLine().split(" ")));

            // Move the first word to the end
            String first = words.removeFirst();
            words.addLast(first.toLowerCase());

            // Make the new first word start with a capital letter
            String newFirst = words.getFirst();
            newFirst = newFirst.substring(0, 1).toUpperCase() + newFirst.substring(1);
            words.set(0, newFirst);

            // Join the words back together into a single string and print the result
            String rephrased = String.join(" ", words);
            System.out.println("I have rephrased that line to read:\n" + rephrased);
        }
    }

}
