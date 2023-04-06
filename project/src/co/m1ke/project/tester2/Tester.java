package co.m1ke.project.tester2;

import co.m1ke.project.content.Movie;
import co.m1ke.project.content.TvShow;
import co.m1ke.project.parser.CsvParser;
import co.m1ke.project.rating.MovieRating;
import co.m1ke.project.rating.TvRating;
import co.m1ke.project.storage.StorageCluster;
import co.m1ke.project.util.Reader;
import co.m1ke.project.util.SteppingIterator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tester {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String path = Reader.read(scanner, "Enter the path to the CSV file: ");
            List<String> lines = CsvParser.readLines(path);
            if (lines == null) {
                System.err.println("Failed to read lines from CSV.");
                return;
            }

            List<TvShow> tvShows = CsvParser.parseLines(CsvParser.extractLinesOfType(lines, "TV Show"));
            List<Movie> movies = CsvParser.parseLines(CsvParser.extractLinesOfType(lines, "Movie"));

            StorageCluster cluster = new StorageCluster();
            cluster.register(TvShow.class, Movie.class);
            cluster.addMany(tvShows, "show_id");
            cluster.addMany(movies, "show_id");

            System.out.println("Successfully loaded " + tvShows.size() + " TV shows and " + movies.size() + " movies from the data file.");

            while (true) {
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
                    TitleSelector mode = Reader.read(scanner, "Please enter the type of title you would like to add:\n"
                            + " - TV Show (tv)\n"
                            + " - Movie (movie)\n", TitleSelector::fromString);

                    if (mode == TitleSelector.TV_SHOW) {
                        TvShow show = create(TitleSelector.TV_SHOW, scanner);
                        cluster.add(show, "show_id");
                        continue;
                    }

                    Movie movie = create(TitleSelector.MOVIE, scanner);
                    cluster.add(movie, "show_id");
                    continue;
                }

                // Remove a title from the store.
                if (opt == MainMenuOptions.DELETE_TITLE) {
                    String show_id = Reader.read(scanner, "Please enter the Show ID of the title you would like to delete:");
                    cluster.remove(show_id);
                    continue;
                }

                // Search for a title by one of it's field values.
                if (opt == MainMenuOptions.SEARCH_TITLE) {
                    TitleSelector selector = Reader.read(scanner, "Please enter the type of title you would like to search for:\n"
                            + " - TV Show (tv)\n"
                            + " - Movie (movie)\n", TitleSelector::fromString);

                    List<Field> fields = new ArrayList<Field>() {
                        {
                            addAll(Arrays.asList(selector.getClazz().getDeclaredFields()));
                            addAll(Arrays.asList(selector.getClazz().getSuperclass().getDeclaredFields()));
                        }
                    };

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

                    target.setAccessible(true);

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

                    if (target.getName().equals("duration")) {
                        handleDurationRanges(unique, cluster, scanner);
                        continue;
                    }

                    System.out.println("Unique values for \"" + target.getName() + "\":");
                    SteppingIterator
                            .with((i, val) -> true, unique)
                            .forEach((i, val) -> System.out.println((i + 1) + ". " + val));

                    String value = Reader.read(scanner, "Please enter the value of the attribute you are searching for:");
                    List<?> results = cluster.search(selector.getClazz(), obj -> {
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

                            if (value.equalsIgnoreCase("None"))
                                return result == null
                                        || result.isEmpty()
                                        || result.trim().isEmpty();

                            return result.equalsIgnoreCase(value)
                                    || result.toLowerCase().contains(value.toLowerCase())
                                    || result.toLowerCase().startsWith(value.toLowerCase());
                        } catch (Exception e) {
                            System.err.println("Exception loading field: " + e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    });

                    if (results.isEmpty()) {
                        System.out.println("No results found.");
                        System.out.println("------------------------------");
                        continue;
                    }

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

    private static <T> T create(TitleSelector mode, Scanner scanner) {
        // Prompt for all information pertaining to the content.
        String show_id = Reader.read(scanner, "Please enter a Show ID:");
        String title = Reader.read(scanner, "Please enter a Title:");
        String director = Reader.read(scanner, "Please enter a Director:");
        String country = Reader.read(scanner, "Please enter a Country:");
        int release_year = Reader.read(scanner, "Please enter a Release Year:", Integer::parseInt);
        int duration = Reader.read(scanner, mode == TitleSelector.TV_SHOW
                ? "Please enter the number of seasons:"
                : "Please enter the duration in minutes:", Integer::parseInt);

        String genre = Reader.read(scanner, "Please enter a Genre:");

        // If the mode is a TV Show, prompt for a TV Rating, otherwise prompt for a Movie Rating - then return the created content.
        if (mode == TitleSelector.TV_SHOW) {
            TvRating rating = Reader.read(scanner, "Please enter a TV Rating:", TvRating::fromString);
            return (T) new TvShow(show_id, title, director, country, release_year, rating, duration, genre);
        }

        MovieRating rating = Reader.read(scanner, "Please enter a MIPAA rating:", MovieRating::fromString);
        return (T) new Movie(show_id, title, director, country, release_year, rating, duration, genre);
    }

}
