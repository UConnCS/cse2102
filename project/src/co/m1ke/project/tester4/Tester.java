package co.m1ke.project.tester4;

import co.m1ke.project.content.Movie;
import co.m1ke.project.content.TvShow;
import co.m1ke.project.content.Watchable;
import co.m1ke.project.parser.CsvParser;
import co.m1ke.project.rating.MovieRating;
import co.m1ke.project.rating.TvRating;
import co.m1ke.project.storage.Storage;
import co.m1ke.project.storage.StorageCluster;
import co.m1ke.project.util.Reader;
import co.m1ke.project.util.SteppingIterator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Tester {

    public static void main(String[] args) {
        // Instantiate a scanner in a try-resource block to auto-close after use.
        try (Scanner scanner = new Scanner(System.in)) {
            // Read the path to the CSV file, and read all the lines in.
            String path = Reader.read(scanner, "Enter the path to the CSV file: ");
            List<String> lines = CsvParser.readLines(path);
            if (lines == null) {
                System.err.println("Failed to read lines from CSV.");
                return;
            }

            // Extract the TV shows and Movies from the lines.
            List<TvShow> tvShows = CsvParser.parseLines(CsvParser.extractLinesOfType(lines, "TV Show"));
            List<Movie> movies = CsvParser.parseLines(CsvParser.extractLinesOfType(lines, "Movie"));

            // Insert both into the storage cluster.
            StorageCluster cluster = new StorageCluster();
            cluster.register(TvShow.class, Movie.class);
            cluster.addMany(tvShows, "show_id");
            cluster.addMany(movies, "show_id");

            // Report how many titles were loaded.
            System.out.println("Successfully loaded " + tvShows.size() + " TV shows and " + movies.size() + " movies from the data file.");

            while (true) {
                // Read the main menu option from the user.
                MainMenuOptions opt = Reader.read(scanner, "Please enter the operation would you like to complete:\n"
                        + " - Add a title (add)\n"
                        + " - Delete a title (delete)\n"
                        + " - Search for titles (search)\n"
                        + " - Modify a title (modify)\n"
                        + " - Exit (exit)\n", MainMenuOptions::fromString);

                // Exit command
                if (opt == MainMenuOptions.EXIT) {
                    break;
                }

                // Add a title to the store
                if (opt == MainMenuOptions.ADD_TITLE) {
                    // Prompt the user to select the type of title they would like to add.
                    TitleSelector mode = Reader.read(scanner, "Please enter the type of title you would like to add:\n"
                            + " - TV Show (tv)\n"
                            + " - Movie (movie)\n", TitleSelector::fromString);

                    // If it is a TV show, create a new TV show and add it to the cluster.
                    if (mode == TitleSelector.TV_SHOW) {
                        TvShow show = create(TitleSelector.TV_SHOW, scanner, cluster);
                        cluster.add(show, "show_id");
                        System.out.println("--------------------");
                        System.out.println("Created: " + show);
                        System.out.println("--------------------");
                        continue;
                    }

                    // Likewise, do the same if the user selected a movie.
                    Movie movie = create(TitleSelector.MOVIE, scanner, cluster);
                    cluster.add(movie, "show_id");
                    System.out.println("--------------------");
                    System.out.println("Created: " + movie);
                    System.out.println("--------------------");
                    continue;
                }

                // Remove a title from the store.
                if (opt == MainMenuOptions.DELETE_TITLE) {
                    // Get all the titles in the cluster.
                    Storage<Watchable> watchables = cluster.abstractify(Movie.class, TvShow.class);
                    List<Watchable> titles = watchables
                            .streamValues()
                            .collect(Collectors.toList());

                    // Setup pagination variables.
                    final AtomicInteger page = new AtomicInteger(1);
                    final int totalPages = (int) Math.ceil(titles.size() / 10.0);

                    // Loop until the user is at the end of the page list, or chooses a title.
                    while (true) {
                        // Compute the page start index and page end index.
                        int pageStart = (page.get() - 1) * 10;
                        int pageEnd = pageStart + 9;

                        // Print out all the titles in the cluster for the [pageStart, pageEnd] range.
                        SteppingIterator
                                .with((i, t) -> i >= pageStart && i <= pageEnd, titles)
                                .forEach((i, t) -> System.out.println((i + 1) + ". " + t.getTitle()));

                        // Read in the ID of the title to delete.
                        String input = Reader.read(scanner, "Please enter the option number associated with the title you want to delete or hit enter to show the next page. Selection: ", str -> {
                            if (str.trim().isEmpty())
                                return page.get() <= totalPages
                                        ? str
                                        : null;

                            try {
                                int i = Integer.parseInt(str) - 1;
                                if (i < 0 || i >= titles.size()) {
                                    throw new Exception("Invalid ID");
                                }

                                return str;
                            } catch (Exception e) {
                                return null;
                            }
                        });

                        // If the user wants to see the next page, and there is one, increment the page.
                        if (input.trim().isEmpty() && page.get() < totalPages) {
                            page.getAndIncrement();
                            continue;
                        }

                        // If the user wants to see the next page, but they are the end, tell them.
                        if (input.trim().isEmpty() && page.get() >= totalPages) {
                            System.out.println("There are no more pages to display, please select an ID to delete.");
                            continue;
                        }

                        // Parse the ID as an integer.
                        int id = Integer.parseInt(input);

                        // Remove the title from the cluster.
                        Watchable title = titles.get(id - 1);
                        cluster.remove(title.getShowId());
                        System.out.println("Removed: (" + id + ") " + title.getTitle());
                        break;
                    }
                }

                // Search for a title by one of it's field values.
                if (opt == MainMenuOptions.SEARCH_TITLE) {
                    // Read in the type of title to search for.
                    TitleSelector selector = Reader.read(scanner, "Please enter the type of title you would like to search for:\n"
                            + " - TV Show (tv)\n"
                            + " - Movie (movie)\n", TitleSelector::fromString);

                    // Get all the fields for the class and it's super class.
                    List<Field> fields = new ArrayList<Field>() {
                        {
                            addAll(Arrays.asList(selector.getClazz().getDeclaredFields()));
                            addAll(Arrays.asList(selector.getClazz().getSuperclass().getDeclaredFields()));
                        }
                    };

                    // Create the prompt string to show the end-user, and then read in the field to search by.
                    String fieldOptions = fields.stream().map(Field::getName).reduce((a, b) -> a + ", " + b).orElse("None");
                    Field target = Reader.read(scanner, "Which attribute are you searching based on?\n" + fieldOptions + "\n", field -> {
                        try {
                            return selector.getClazz().getDeclaredField(field);
                        } catch (NoSuchFieldException ignored) {
                        }

                        try {
                            return selector.getClazz().getSuperclass().getDeclaredField(field);
                        } catch (NoSuchFieldException e) {
                            return null;
                        }
                    });

                    // Set the field to be accessible, and then read in the value to search for.
                    target.setAccessible(true);

                    // Collect all distinct values for the field,  and then keep it in a sorted list.
                    List<String> unique = cluster
                            .getCluster(selector.getClazz())
                            .streamValues()
                            .map(obj -> {
                                try {
                                    Object val = target.get(obj);
                                    String result = "";
                                    if (obj instanceof String)
                                        result = (String) obj;
                                    if (obj instanceof TvRating)
                                        result = ((TvRating) obj).getName();
                                    if (obj instanceof MovieRating)
                                        result = ((MovieRating) obj).getName();
                                    else result = String.valueOf(val);
                                    return result;
                                } catch (Exception e) {
                                    return null;
                                }
                            })
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList());

                    // If the field is the duration, then handle it differently with the ranges selector.
                    if (target.getName().equals("duration")) {
                        handleDurationRanges(unique, cluster, scanner);
                        continue;
                    }

                    // Print out all the unique values for the field as part of the prompt.
                    System.out.println("Unique values for \"" + target.getName() + "\":");
                    SteppingIterator
                            .with((i, val) -> true, unique)
                            .forEach((i, val) -> System.out.println((i + 1) + ". " + val));

                    // Read in the value to search for.
                    String value = Reader.read(scanner, "Please enter the value of the attribute you are searching for:");

                    // Search the cluster for the value, and then print out the results.
                    List<?> results = cluster.search(selector.getClazz(), obj -> {
                        try {
                            // Get the value of the field, and then convert it to a string.
                            Object val = target.get(obj);
                            String result = "";
                            if (obj instanceof String)
                                result = (String) obj;
                            if (obj instanceof TvRating)
                                result = ((TvRating) obj).getName();
                            if (obj instanceof MovieRating)
                                result = ((MovieRating) obj).getName();
                            else result = String.valueOf(val);

                            // If the value is "None", then check if the result is null or empty.
                            if (value.equalsIgnoreCase("None"))
                                return result == null
                                        || result.isEmpty()
                                        || result.trim().isEmpty();

                            // Otherwise, check if the result is equal to the value, or if it contains the value.
                            return result.equalsIgnoreCase(value)
                                    || result.toLowerCase().contains(value.toLowerCase())
                                    || result.toLowerCase().startsWith(value.toLowerCase());
                        } catch (Exception e) {
                            System.err.println("Exception loading field: " + e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    });

                    // If there are no results, then print out a message.
                    if (results.isEmpty()) {
                        System.out.println("No results found.");
                        System.out.println("------------------------------");
                        continue;
                    }

                    // Otherwise, print out the results.
                    System.out.println("Found " + results.size() + " results:");
                    results.forEach(result -> {
                        System.out.println("------------------------------");
                        System.out.println(result);
                    });

                    System.out.println("------------------------------");
                }
            }
        }

    }

    private static void handleDurationRanges(List<String> unique, StorageCluster cluster, Scanner scanner) {
        // Converts the duration strings to integers.
        List<Integer> durations = unique
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // Creates a list of ranges based on the durations.
        List<String> ranges = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int min = i * 30;
            int max = (i + 1) * 30;

            // Checks if there are any durations in the list that match this interval, if so add the interval.
            if (durations.stream().anyMatch(duration -> duration >= min && duration <= max))
                ranges.add(min + "-" + max + " minutes");
        }

        // Creates the prompt text that is shown to the user.
        StringBuilder prompt = new StringBuilder("Please select a duration range:\n");
        for (int i = 0; i < ranges.size(); i++) {
            String range = ranges.get(i);
            prompt
                    .append((i + 1))
                    .append(". ")
                    .append(range)
                    .append("\n");
        }

        // Reads in the user input for the index in the prompt associated with the duration interval.
        int selectedRange = Reader.read(scanner, prompt.toString(), range -> {
            try {
                int index = Integer.parseInt(range);
                if (index < 1 || index > ranges.size())
                    return null;

                return index - 1;
            } catch (NumberFormatException e) {
                return null;
            }
        });

        // Gets the selected range, and parses out the min/max values.
        String range = ranges.get(selectedRange);
        String[] split = range.split("-");
        int min = Integer.parseInt(split[0].replace(" minutes", ""));
        int max = Integer.parseInt(split[1].replace(" minutes", ""));

        // Searches the cluster to find all movies that match the duration interval.
        List<?> results = cluster.search(Movie.class, movie -> {
            int duration = movie.getDuration();
            return duration >= min && duration <= max;
        });

        // There are no results
        if (results.isEmpty()) {
            System.out.println("No results found.");
            System.out.println("------------------------------");
            return;
        }

        // There are results, print them out.
        System.out.println("Found " + results.size() + " results:");
        results.forEach(result -> {
            System.out.println("------------------------------");
            System.out.println(result);
        });

        System.out.println("------------------------------");
    }

    private static <T> T create(TitleSelector mode, Scanner scanner, StorageCluster cluster) {
        // Get the next available ID for the content by getting all the watchables, streaming their show_id's, and then incrementing it by one.
        Storage<Watchable> watchables = cluster.abstractify(Movie.class, TvShow.class);
        String show_id = watchables
                .streamKeys()
                .map(id -> id.substring(1))
                .map(Integer::parseInt)
                .max(Integer::compareTo)
                .map(id -> "s" + (id + 1))
                .orElse("s0");

        // Prompt for all information pertaining to the content.
        String title = Reader.read(scanner, "Please enter a title:");
        String director = Reader.read(scanner, "Please enter a director:");
        String country = Reader.read(scanner, "Please enter a country:");
        int release_year = Reader.read(scanner, "Please enter a release year:", Integer::parseInt);
        int duration = Reader.read(scanner, mode == TitleSelector.TV_SHOW
                ? "Please enter the number of seasons:"
                : "Please enter the duration in minutes:", Integer::parseInt);

        String genre = Reader.read(scanner, "Please enter the genres (separated by commas):");

        // If the mode is a TV Show, prompt for a TV Rating, otherwise prompt for a Movie Rating - then return the created content.
        if (mode == TitleSelector.TV_SHOW) {
            TvRating rating = Reader.read(scanner, "Please enter a TV Rating:", TvRating::fromString);
            return (T) new TvShow(show_id, title, director, country, release_year, rating, duration, genre);
        }

        MovieRating rating = Reader.read(scanner, "Please enter a MIPAA rating:", MovieRating::fromString);
        return (T) new Movie(show_id, title, director, country, release_year, rating, duration, genre);
    }

}
