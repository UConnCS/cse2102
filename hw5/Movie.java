package hw5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Movie {

    // Instantiate instance variables, such as name, MIPAA rating, and ratings map
    private String name;
    private MipaaRating rating;
    private Map<Integer, Integer> ratings;

    // Create an enum for the MIPAA ratings so we can validate against it later
    public enum MipaaRating {
        G("G"),
        PG("PG"),
        PG_13("PG-13"),
        R("R"),
        NC_17("NC-17");

        private String name;

        // Private constructor for above enum constants
        MipaaRating(String rating) {
            this.name = rating;
        }

        // Return the name of the current MipaaRating
        public String getName() {
            return name;
        }

        // Helper method to translate a string -> MipaaRating constant
        public static MipaaRating fromString(String input) {
            return Arrays
                .stream(values())
                .filter(rating -> rating.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
        }
    }

    // Constructor for Movie class, takes in a name and MipaaRating
    public Movie(String name, MipaaRating rating) {
        this.name = name;
        this.rating = rating;
        this.ratings = new HashMap<Integer, Integer>() {
            {
                put(1, 0);
                put(2, 0);
                put(3, 0);
                put(4, 0);
                put(5, 0);
            }
        };
    }

    /**
     * Adds a rating to the movie by taking in
     * a rating from 1-5, then finds and increments
     * the associated rating in the ratings map.
     * 
     * @param rating the rating (1-5)
     */
    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Invalid rating");
        }

        int currentRating = ratings.get(rating);
        ratings.put(rating, currentRating + 1);
    }

    // Returns the name of the movie object
    public String getName() {
        return name;
    }

    // Sets the name of the movie object
    public void setName(String name) {
        this.name = name;
    }

    // Returns the MipaaRating of the movie object
    public MipaaRating getRating() {
        return rating;
    }

    // Sets the MipaaRating of the movie object
    public void setRating(MipaaRating rating) {
        this.rating = rating;
    }

    /**
     * Computes the average rating of the movie based on
     * all of the entries in the ratings map.
     * 
     * Achieved by streaming over the entry-set of the ratings map,
     * multiplying the key (rating) by the value (number of ratings),
     * then summing all of the values. This is then divided by the
     * total number of collected ratings.
     * 
     * @return the average rating for the movie
     */
    public double getAverage() {
        return ratings
            .entrySet()
            .stream()
            .mapToInt(entry -> entry.getKey() * entry.getValue())
            .sum() / (double) ratings
                .entrySet()
                .stream()
                .mapToInt(entry -> entry.getValue())
                .sum();
    }

}