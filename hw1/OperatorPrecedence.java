package hw1;

import java.text.DecimalFormat;

public class OperatorPrecedence {
    
    // Declare the DecimalFormat object that will be used to format the output
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    public static void main(String[] args) {
        // Verify that the user provided three arguments
        if (args.length != 3) {
            System.out.println("Missing arguments.");
            System.exit(1);
        }

        // Verify that all provided arguments can be parsed as integers
        if (!isInt(args[0]) || !isInt(args[1]) || !isInt(args[2])) {
            System.out.println("Arguments must be integers.");
            System.exit(1);
        }

        // Parse the arguments as integers
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);

        // Compute the cube root of x^2 + y^2 - |z|, and then print the result
        double result = Math.cbrt(Math.pow(x, 2) + Math.pow(y, 2) - Math.abs(z));
        System.out.println("Cube root is " + DF.format(result));
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

}
