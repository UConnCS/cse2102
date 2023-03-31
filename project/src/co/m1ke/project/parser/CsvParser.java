package co.m1ke.project.parser;

import co.m1ke.project.content.Movie;
import co.m1ke.project.content.TvShow;
import co.m1ke.project.content.Watchable;
import co.m1ke.project.rating.MovieRating;
import co.m1ke.project.rating.TvRating;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class parses CSV files into a list of typed Watchable subclasses, and
 * provides broad utilities to read from files, and parse CSV data.
 */
@SuppressWarnings("unchecked")
public class CsvParser {

    // Mappings of CSV data -> TvShow, describes how the CSV line segments should be parsed into valid types to construct TvShow objects.
    private static List<Mapper<?>> TV_TYPES = new ArrayList<Mapper<?>>() {
        {
            add(Mapper.of(0, String::toString, true)); // show_id
            add(Mapper.of(1, String::toString, false)); // type
            add(Mapper.of(2, String::toString, true)); // title
            add(Mapper.of(3, String::toString, true)); // director
            add(Mapper.of(4, String::toString, true)); // country
            add(Mapper.of(5, Integer::parseInt, true)); // release_year
            add(Mapper.of(6, TvRating::fromString, true)); // rating
            add(Mapper.of(7, seasons -> Integer.parseInt(seasons.trim().length() > 0 ? seasons.split(" Season")[0] : "-1"), true)); // duration
            add(Mapper.of(8, String::toString, true)); // genre
        }
    };

    // Mappings of CSV data -> Movie, describes how the CSV line segments should be parsed into valid types to construct Movie objects.
    private static List<Mapper<?>> MOVIE_TYPES = new ArrayList<Mapper<?>>() {
        {
            add(Mapper.of(0, String::toString, true)); // show_id
            add(Mapper.of(1, String::toString, false)); // type
            add(Mapper.of(2, String::toString, true)); // title
            add(Mapper.of(3, String::toString, true)); // director
            add(Mapper.of(4, String::toString, true)); // country
            add(Mapper.of(5, Integer::parseInt, true)); // release_year
            add(Mapper.of(6, MovieRating::fromString, true)); // rating
            add(Mapper.of(7, minutes -> Integer.parseInt(minutes.trim().length() > 0 ? minutes.split(" min")[0] : "-1"), true)); // duration
            add(Mapper.of(8, String::toString, true)); // genre
        }
    };

    /**
     * Reads all lines from a file, and returns them as a List containing Strings.
     * @param path The path to the file to read.
     * @return A List containing all lines from the file.
     */
    public static List<String> readLines(String path) {
        try {
            // Instantiate a new File object, and check if it exists.
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }

            // Read all lines from the file into a BufferedReader, then skip the first line (the header line), and return the rest.
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader
                    .lines()
                    .skip(1)
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            // Handle any errors that arise when reading in the file.
            System.err.println("Error reading file: " + path);
            return null;
        }
    }

    /**
     * Helper method to parse an entire file's lines into Watchable objects.
     * @param lines The lines to parse.
     * @param <T> The type of Watchable to parse.
     * @return A List containing all parsed Watchable objects.
     */
    public static <T extends Watchable> List<T> parseLines(List<String> lines) {
        return lines.stream()
                .map(line -> (T) parseLine(line))
                .collect(Collectors.toList());
    }

    /**
     * Filters out all lines of a certain type into a list.
     * Useful for separating all TV show lines from Movie lines.
     *
     * @param lines The lines to filter.
     * @param type The type to filter for.
     * @return A List containing all lines of the specified type.
     */
    public static List<String> extractLinesOfType(List<String> lines, String type) {
        return lines.stream()
                .filter(line -> line.contains(type))
                .collect(Collectors.toList());
    }

    /**
     * Parses a given line into a Watchable-like object.
     * @param line The line to parse.
     * @param <T> The type of Watchable to convert to.
     * @return A Watchable-like object.
     */
    public static <T extends Watchable> T parseLine(String line) {
        try {
            // Split the line by commas, and combine any quoted values, such as country and genre.
            String[] values = combineQuoted(line.split(","));

            // Determine the type of the line, and use the appropriate mapping.
            String type = values[1].equalsIgnoreCase("TV Show")
                    ? "TV"
                    : "MOVIE";

            // Get the mappings for the determined type.
            List<Mapper<?>> types = type.equalsIgnoreCase("TV")
                    ? TV_TYPES
                    : MOVIE_TYPES;

            // Convert the values into a List, and proceed with assembling the object.
            List<String> vals = Arrays.asList(values);

            // If the type is TV, return a TvShow, otherwise return a Movie.
            if (type.equals("TV")) return (T) assemble(vals, types, TvShow.class);
            return (T) assemble(vals, types, Movie.class);
        } catch (Exception exception) {
            // Handle any errors that arise when parsing the line.
            System.err.println("Error parsing line: " + line);
            return null;
        }
    }

    /**
     * Reflectively assembles a Watchable-like object from a list of values and a list of mappings.
     *
     * @param values The values to map.
     * @param types The mappings to use.
     * @param clazz The class to instantiate.
     * @param <T> The type of Watchable to convert to.
     *
     * @return A Watchable-like object.
     */
    private static <T extends Watchable> T assemble(List<String> values, List<Mapper<?>> types, Class<T> clazz) {
        try {
            // Find the first declared constructor for the target class type.
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];

            // Invoke Constructor<T>#newInstance(), and convert the list of
            // types into the mapped values using the above defined Mappers.
            return (T) constructor.newInstance(types
                    .stream()
                    .filter(Mapper::isConstructorArg)
                    .map(mapper -> mapper.map(values.get(mapper.getTarget())))
                    .toArray());
        } catch (Exception e) {
            // Handle any errors that arise when assembling the object.
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Combines any quoted values, such as country and genre, into a single value.
     * @param values The values to combine.
     * @return An array of combined values.
     */
    private static String[] combineQuoted(String[] values) {
        // Instantiate a new ArrayList to store the new values, and a StringBuilder to store the current value.
        List<String> newValues = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        // Keep track of whether or not we're in a quoted value.
        boolean inQuote = false;

        // Iterate over all values, and combine any quoted values.
        for (String value : values) {
            // If the value starts with a quote, and we're not already in a quote, set the inQuote flag to true, and append the value to the StringBuilder.
            if (value.startsWith("\"")) {
                inQuote = true;
                sb.append(value.substring(1));
                continue;
            }

            // If the value ends with a quote, and we're in a quote, set the inQuote flag to false, and append the value to the StringBuilder.
            if (value.endsWith("\"")) {
                inQuote = false;
                sb.append(",").append(value.substring(0, value.length() - 1));
                newValues.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }

            // If we're in a quote, append the value to the StringBuilder.
            if (inQuote) {
                sb.append(",").append(value);
                continue;
            }

            // Otherwise, just add the value to the new values list.
            newValues.add(value);
        }

        // Return the new values as an array.
        return newValues.toArray(new String[0]);
    }

}
