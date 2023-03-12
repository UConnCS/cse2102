package hw5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import hw5.Documentary.TvRating;
import hw5.Movie.MipaaRating;

public class TestDocumentaries {
    
    public static void main(String[] args) {
        // Use a try-with-resource statement to create and auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user for the file name
            System.out.print("Please enter the documentaries filename: ");

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

            // Store the Documentaries and DocumentarySeries
            List<Documentary> documentaries = new ArrayList<>();
            List<DocumentarySeries> series = new ArrayList<>();

            // Read the file line by line, and parse the data as we go along.
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach(line -> {
                // Split by commas to retrieve list of attributes
                String[] attributes = line.split(", ");
                boolean extras = attributes.length > 3;
                
                // Parse the attributes
                String name = attributes[0];
                MipaaRating rating = MipaaRating.fromString(attributes[1]);
                TvRating tvRating = TvRating.fromString(attributes[2]);

                // Validate the ratings match the expected values
                if (rating == null || tvRating == null) {
                    System.out.println("Invalid rating type provided for " + name);
                    return;
                }

                // If there are no extras, then we are dealing with a Documentary
                if (!extras) {
                    // Create a new Documentary object and add it to the list
                    Documentary doc = new Documentary(name, rating, tvRating);
                    documentaries.add(doc);
                    return;
                }

                // Validate the number of episodes and seasons are integers
                if (!isInt(attributes[3]) || !isInt(attributes[4])) {
                    System.out.println("Non-numeric number of episodes or seasons provided for " + name);
                    return;
                }

                // Parse the number of episodes and seasons
                int seasons = Integer.parseInt(attributes[3]);
                int episodes = Integer.parseInt(attributes[4]);

                // Create a new DocumentarySeries object and add it to the list
                DocumentarySeries docSeries = new DocumentarySeries(name, rating, tvRating, episodes, seasons);
                series.add(docSeries);
            });

            // Output the number of documentaries and series
            System.out.println("Number of Documentaries: " + documentaries.size());
            System.out.println("Number of Documentary Series: " + series.size());

            // Output the number of documentaries and series with a specific rating
            System.out.println("Number of R Documentaries: " + countByRating(documentaries, series, doc -> doc.getRating() == MipaaRating.R));
            System.out.println("Number of TV-14 Documentaries: " + countByRating(documentaries, series, doc -> doc.getTvRating() == TvRating.TV_14));
            System.out.println("Number of TV-MA Documentaries: " + countByRating(documentaries, series, doc -> doc.getTvRating() == TvRating.TV_MA));

            // Find the documentary series with the highest number of seasons and episodes
            DocumentarySeries maxSeasons = series
                .stream()
                .max((a, b) -> a.getSeasons() - b.getSeasons())
                .get();

            DocumentarySeries maxEpisodes = series
                .stream()
                .max((a, b) -> a.getEpisodes() - b.getEpisodes())
                .get();

            // Check if the values are null, and if not, output the results
            if (maxSeasons != null) System.out.println(maxSeasons.getName() + " is the documentary series with the highest number of seasons: " + maxSeasons.getSeasons());
            if (maxEpisodes != null) System.out.println(maxEpisodes.getName() + " is the documentary series with the highest number of episodes: " + maxEpisodes.getEpisodes());

            // Close the reader
            reader.close();
        } catch (IOException ex) {
            // In case an exception occurs, print it to the console
            System.err.printf("An exception occurred: %s", ex.getMessage());
        }
    }

    /**
     * Counts the number of documentaries and series that match the provided filter
     * @param documentaries the documentaries list
     * @param series the documentary series' list
     * @param filter the filter to apply
     * @return the amount of items that matched the filter between the two lists
     */
    private static int countByRating(List<Documentary> documentaries, List<DocumentarySeries> series, Predicate<Documentary> filter) {
        int docs = documentaries
            .stream()
            .filter(filter)
            .toArray().length;

        int docSeries = series
            .stream()
            .filter(filter)
            .toArray().length;

        return docs + docSeries;
    }

    /**
     * Checks if a string is an integer
     * @param str the string to check
     * @return true if the string is an integer, false otherwise
     */
    private static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
