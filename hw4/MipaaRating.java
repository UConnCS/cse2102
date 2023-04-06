package hw4;

import java.util.Arrays;

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