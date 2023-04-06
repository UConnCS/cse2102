package hw4;

import java.util.Scanner;

public class MovieTestRating {
    
    public static void main(String[] args) {
        // Use a try-resource block to both construct and auto-close the scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user to input a movie name
            System.out.print("Enter the name of the movie: ");
            String name = scanner.nextLine();

            // Prompt the user to input a MIPAA rating
            System.out.print("Enter the MIPAA rating of the movie: ");
            String mipaaRating = scanner.nextLine();

            // Validate the inputted MIPAA rating
            MipaaRating rating = MipaaRating.fromString(mipaaRating);
            if (rating == null) {
                System.out.println("Invalid MIPAA rating");
                return;
            }

            // Construct the movie object
            Movie movie = new Movie(name, rating);
            
            // Prompt for ratings, exit once rating "-1" is entered
            while (true) {
                System.out.print("Enter a rating (1-5) or -1 to exit: ");
                String ratingInput = scanner.nextLine();
                if (ratingInput.equals("-1")) {
                    break;
                }

                if (!ratingInput.matches("[1-5]")) {
                    System.out.println("Invalid rating");
                    continue;
                }

                // Validate the inputted rating
                movie.addRating(Integer.parseInt(ratingInput));
            }

            // Print the movie's name, MIPAA rating, and average ratings
            System.out.println("Name of the movie: " + movie.getName());
            System.out.println("MIPAA rating: " + movie.getRating().getName());
            System.out.println("Average rating: " + movie.getAverage());
        }
    }

}
