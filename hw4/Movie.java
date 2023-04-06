package hw4;

import java.util.HashMap;
import java.util.Map;

public class Movie {

    // Instantiate instance variables, such as name, MIPAA rating, and ratings map
    private String name;
    private MipaaRating rating;
    private Map<Integer, Integer> ratings;

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

    // Returns the MipaaRating of the movie object
    public MipaaRating getRating() {
        return rating;
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