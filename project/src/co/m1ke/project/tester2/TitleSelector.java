package co.m1ke.project.tester2;

import co.m1ke.project.content.Movie;
import co.m1ke.project.content.TvShow;

import java.util.Arrays;

public enum TitleSelector {

    // Enum constants for the various possible tester modes
    TV_SHOW(TvShow.class, "tv"), MOVIE(Movie.class, "movie");

    // Private fields for the above enum constants
    private Class<?> clazz;
    private String displayName;

    // Private constructor for above enum constants
    TitleSelector(Class<?> clazz, String displayName) {
        this.clazz = clazz;
        this.displayName = displayName;
    }

    /**
     * Returns the target class for the selected TesterMode.
     * @return The target class for the selected TesterMode.
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * Returns the display name for the selected TesterMode.
     * @return The display name for the selected TesterMode.
     */
    public String getDisplayName() {
        return displayName;
    }

    // Helper method to translate a string -> TesterMode constant
    public static TitleSelector fromString(String value) {
        return Arrays
                .stream(values())
                .filter(mode -> mode.getDisplayName().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

}
