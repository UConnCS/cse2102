package hw1;

import java.text.DecimalFormat;

public class Force {
    
    private static final double GRAV_CONST = 6.67e-11;
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    public static void main(String[] args) {
        // Verify that the user provided three arguments
        if (args.length != 3) {
            System.out.println("Missing arguments.");
            System.exit(1);
        }

        // Verify that all provided arguments can be parsed as integers
        if (!isDouble(args[0]) || !isDouble(args[1]) || !isDouble(args[2])) {
            System.out.println("Arguments must be integers.");
            System.exit(1);
        }

        // Parse the arguments as integers
        double mass1 = Double.parseDouble(args[0]);
        double mass2 = Double.parseDouble(args[1]);
        double rad = Double.parseDouble(args[2]);

        // Compute [F = G * (m1 * m2 / (r * r))] and print the result
        double force = GRAV_CONST * (mass1 * mass2 / (rad * rad));
        System.out.println("Force is " + DF.format(force));
    }

    /**
     * Attempt to parse the provided input using {@link Double#parseInt(String)}.
     * If an exception is thrown, the input cannot be parsed as an Double.
     * 
     * @param input The input to be parsed
     * @return true if the provided input can be parsed as a double, false otherwise
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
