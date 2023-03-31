package co.m1ke.project.rating;

import java.util.Arrays;

public enum MovieRating {

    // Enum constants for the various possible MIPAA ratings
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");

    private String name;

    // Private constructor for above enum constants
    MovieRating(String rating) {
        this.name = rating;
    }

    // Return the name of the current ContentRating
    public String getName() {
        return name;
    }

    // Helper method to translate a string -> MovieRating constant
    public static MovieRating fromString(String input) {
        return Arrays
                .stream(values())
                .filter(rating -> rating.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
    }

}
