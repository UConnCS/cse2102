package co.m1ke.project.rating;

import java.util.Arrays;

public enum TvRating {

    // Enum constants for the various possible TV ratings
    TV_Y("TV-Y"),
    TV_Y7("TV-Y7"),
    TV_G("TV-G"),
    TV_PG("TV-PG"),
    TV_14("TV-14"),
    TV_MA("TV-MA");

    private String name;

    // Private constructor for above enum constants
    TvRating(String rating) {
        this.name = rating;
    }

    // Return the name of the current ContentRating
    public String getName() {
        return name;
    }

    // Helper method to translate a string -> TvRating constant
    public static TvRating fromString(String input) {
        return Arrays
                .stream(values())
                .filter(rating -> rating.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
    }

}
