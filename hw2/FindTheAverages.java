package hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class FindTheAverages {
    
    public static void main(String[] args) {
        // Use a try-with-resource statement to create and auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user for the file name
            System.out.println("Enter the name of the input file:");

            // Read the file name from the user, and ensure it is actually provided
            String fileName = scanner.nextLine();
            if (fileName == null || fileName.isEmpty()) {
                System.out.println("No file name provided");
                return;
            }

            // Instantiate a File object with the provided path, and ensure it exists
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("File does not exist");
                return;
            }


            // Read the file line by line, and store the contents in a StringBuilder
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            reader.lines().forEach(line -> builder.append(line + "\n"));
            reader.close();

            // Stream the lines, then for each line split it to get the student name
            // and subsequently all of the test scores. Then, map the test scores to
            // doubles, and average them. Finally, create all of the output strings
            // and print them to the console.
            Arrays
                .stream(builder
                    .toString()
                    .split("\n"))
                .map(line -> {
                    String student = line.split(", ")[0];
                    double average = Arrays
                        .stream(line.split(", "))
                        .skip(1)
                        .mapToDouble(Double::parseDouble)
                        .average()
                        .getAsDouble();

                    return String.format("The average score for %s is %.2f", student, average);
                })
                .forEach(System.out::println);
        } catch (IOException ex) {
            // In case an exception occurs, print it to the console
            System.err.printf("An exception occurred: %s", ex.getMessage());
        }
    }

}
