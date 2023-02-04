package hw1;

import java.text.DecimalFormat;

public class AreaFromCircumference {

    // Declare PI, and the DecimalFormat object that will be used to format the output
    private static final double PI = 22 / 7;
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    public static void main(String[] args) {
        // Verify that the user provided a circumference argument
        if (args.length != 1) {
            System.out.println("Missing circumference argument.");
            System.exit(1);   
        }

        // Verify that the provided argument can be parsed as a double
        if (!isDouble(args[0])) {
            System.out.println("Circumference argument must be a number.");
            System.exit(1);
        }

        // Parse args[0] as a double, compute the area using the formula, and then print the result
        double circumference = Double.parseDouble(args[0]);
        double area = (circumference * circumference) / (4 * PI);
        System.out.println("Area of circle is: " + DF.format(area));
    }

    /**
     * Attempt to parse the provided input using {@link Double#parseDouble(String)}.
     * If an exception is thrown, the input cannot be parsed as a double.
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