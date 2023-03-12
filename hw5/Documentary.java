package hw5;

import java.util.Arrays;

public class Documentary extends Movie {
    
    // Instantiate instance variable, which in this case is the TvRating
    private TvRating tvRating;

    // Create an enum for the TV ratings so we can validate against it later
    public enum TvRating {
        TV_Y("TV-Y"),
        TV_Y7("TV-Y7"),
        TV_G("TV-G"),
        TV_PG("TV-PG"),
        TV_14("TV-14"),
        TV_MA("TV-MA");

        private String name;

        // Private constructor for above enum constants
        TvRating(String name) {
            this.name = name;
        }

        // Return the name of the current TvRating
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

    /**
     * Constructor for the Documentary class, takes in a name, MipaaRating, and TvRating.
     * 
     * @param name The name of the movie
     * @param rating The MipaaRating of the movie
     * @param tvRating The TvRating of the movie
     */
    public Documentary(String name, MipaaRating rating, TvRating tvRating) {
        super(name, rating);
        this.tvRating = tvRating;
    }

    /**
     * Returns the TV rating for the movie
     * @return The TV rating for the movie
     */
    public TvRating getTvRating() {
        return tvRating;
    }

    /**
     * Sets the TV rating for the movie
     * @param tvRating The TV rating to set
     */
    public void setTvRating(TvRating tvRating) {
        this.tvRating = tvRating;
    }

    /**
     * Prints the values for all instance variables associated
     * with both the Documentary class, and it's superclass Movie.
     */
    public void writeOutput() {
        System.out.println("Documentary: " + getName());
        System.out.println("MIPAA Rating: " + getRating().getName());
        System.out.println("TV Rating: " + getTvRating().getName());
        System.out.println("Average Rating: " + getAverage());
    }

}
