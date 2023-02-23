package hw3;

import java.util.Scanner;

public class BMIClassification {

    public static void main(String[] args) {
        // Use a try-with-resource statement to create and auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user for their weight and capture their input
            System.out.print("Enter your weight in pounds: ");
            String weight = scanner.nextLine();

            // Prompt the user for their height and capture their input
            System.out.print("Enter your height in inches: ");
            String height = scanner.nextLine();

            // Ensure that the user's input is valid for both inputs
            if (!isDouble(weight) || !isDouble(height)) {
                System.out.println("Invalid Input!");
                return;
            }

            // Convert the user's height and weight to doubles
            double heightIn = Double.parseDouble(height), weightLb = Double.parseDouble(weight);
            
            // Convert those doubles to meters and kilograms respectively
            double heightM = heightIn * 0.025, weightKg = weightLb * 0.45;

            // Compute the BMI according to BMI = weight / (height^2)
            double bmi = weightKg / (heightM * heightM);

            // Print the results, and their risk category
            System.out.println("Your BMI is " + bmi);
            System.out.printf("Your risk category is %s\n", classifyBmi(bmi));
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

    /**
     * Classifies the provided BMI value according to the following table:
     * 
     * BMI < 18.5: Underweight
     * 18.5 <= BMI < 25: Normal weight
     * 25 <= BMI < 30: Overweight
     * BMI >= 30: Obese
     * 
     * @param input The BMI value to be classified
     * @return The risk category of the provided BMI value
     */
    private static String classifyBmi(double input) {
        if (input < 18.5)
            return "Underweight";
        else if (input < 25)
            return "Normal weight";
        else if (input < 30)
            return "Overweight";
        else
            return "Obese";
    }

}
