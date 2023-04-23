package co.m1ke.project.util;

import java.util.Scanner;
import java.util.function.Function;

public class Reader {

    // Prompts the user to input a string, and returns it.
    public static String read(Scanner scanner, String prompt) {
        return read(scanner, prompt, (s) -> s);
    }

    /**
     * Prompts the user to input a value of type T, then uses the converter function to convert it to the desired type.
     *
     * @param scanner The scanner to read from.
     * @param prompt The prompt to display to the user.
     * @param converter The function to convert the input to the desired type.
     * @param <T> The type to convert the input to.
     *
     * @return The converted input.
     */
    public static <T> T read(Scanner scanner, String prompt, Function<String, T> converter) {
        // If the prompt is null, set it to the default value.
        if (prompt == null) prompt = "Enter value: ";

        // If the prompt doesn't end with a space, add one.
        if (!prompt.endsWith(" ")) prompt += " ";

        // Print the prompt.
        System.out.print(prompt);

        try {
            // Read the input, convert it, and return it.
            T object = converter.apply(scanner.nextLine());

            // If the object is null or cannot be converted, throw an exception.
            if (object == null) throw new InvalidInput("Invalid input");

            // Otherwise, return the object.
            return object;
        } catch (Exception ex) {
            // If the exception is an InvalidInput, get the message from it.
            String message = "Invalid parameters";
            if (ex.getClass().isAssignableFrom(InvalidInput.class)) {
                final String invalidMessage = ex.getMessage();
                if (!invalidMessage.equals("")) message = invalidMessage;
            }

            // Print the error message, and then reprompt the user for a valid input.
            System.out.println(message);
            return read(scanner, prompt, converter);
        }
    }

    public static <T> T process(String prompt, Scanner scanner, String input, Function<String, T> converter) {
        try {
            // Read the input, convert it, and return it.
            T object = converter.apply(input);

            // If the object is null or cannot be converted, throw an exception.
            if (object == null) throw new InvalidInput("Invalid input");

            // Otherwise, return the object.
            return object;
        } catch (Exception ex) {
            // If the exception is an InvalidInput, get the message from it.
            String message = "Invalid parameters";
            if (ex.getClass().isAssignableFrom(InvalidInput.class)) {
                final String invalidMessage = ex.getMessage();
                if (!invalidMessage.equals("")) message = invalidMessage;
            }

            // Print the error message, and then reprompt the user for a valid input.
            System.out.println(message);
            return read(scanner, prompt, converter);
        }
    }

    /**
     * Wrapper over RuntimeException to use for invalid input.
     */
    public static class InvalidInput extends RuntimeException {

        public InvalidInput(String message) {
            super(message);
        }

    }

}