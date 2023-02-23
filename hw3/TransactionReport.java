package hw3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class TransactionReport {
    
    private static final DecimalFormat DF = new DecimalFormat("#.00");

    public static void main(String[] args) {
        // Use a try-with-resource statement to create and auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user for the file name
            System.out.print("Please enter the transaction filename: ");

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

            // Keep a counter variable to track of the total amount sold
            AtomicReference<Double> total = new AtomicReference(0D);

            // Stream the lines, then for each line, split by a comma as this is
            // csv-formatted. The format is SKU,Quantity,Price,Description. Print
            // out all of the components, and then the total sold.
            Arrays
                .stream(builder
                    .toString()
                    .split("\n"))
                .skip(1)
                .map(line -> {
                    // Decode the line's contents
                    List<String> parts = Arrays.asList(line.split(","));
                    String sku = parts.get(0);
                    String description = parts.get(3);
                    double quantity = Double.parseDouble(parts.get(1));
                    double price = Double.parseDouble(parts.get(2));
                    double amount = quantity * price;

                    // Add the amount to the total
                    total.getAndUpdate(val -> val + amount);

                    return String.format(
                        "Sold %.0f of %s (SKU: %s) at $%s each. Sale is $%s",
                        quantity, description, sku, DF.format(price), DF.format(amount)
                    );
                })
                .forEach(System.out::println);

            // Print out the total amount sold
            System.out.println("Total sales: $" + DF.format(total.get()));
        } catch (IOException ex) {
            // In case an exception occurs, print it to the console
            System.err.printf("An exception occurred: %s", ex.getMessage());
        }
    }

}
