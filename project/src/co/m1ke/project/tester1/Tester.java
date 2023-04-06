package co.m1ke.project.tester1;

import co.m1ke.project.content.Movie;
import co.m1ke.project.content.TvShow;
import co.m1ke.project.rating.MovieRating;
import co.m1ke.project.rating.TvRating;
import co.m1ke.project.util.Reader;

import java.lang.reflect.Field;
import java.util.Scanner;

// Tester used to demo Spring 1 for TAs.
public class Tester {

    public static void main(String[] args) {
        // Instantiate a scanner with a try-resource block to auto-close the scanner once finished.
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user for a mode, read it, validate it, and then map it to a TesterMode enum constant.
            TesterMode mode = Reader.read(scanner, "Would you like to build a TV Show or a Movie?", val -> {
                if (val.equalsIgnoreCase("tv show")) return TesterMode.TV_SHOW;
                if (val.equalsIgnoreCase("movie")) return TesterMode.MOVIE;
                return TesterMode.fromString(val.toUpperCase());
            });

            // Report to the user the type of content they are building.
            System.out.println("Okay, we are going to build a " + mode.getDisplayName() + ".");

            // If the mode is a TV Show
            if (mode == TesterMode.TV_SHOW) {
                // Start the routine to prompt the user for all information pertaining to the show, then print it once finished.
                TvShow tvShow = create(mode, scanner);
                System.out.println(tvShow);

                // Prompt the user if they would like to modify any of the attributes, then modify them if they do.
                TvShow updatedTv = modify(tvShow, scanner);
                System.out.println("The final TV Show is: \n" + updatedTv);
                return;
            }

            // If the mode is a Movie, do the same as above.
            Movie movie = create(mode, scanner);
            System.out.println(movie);

            // Prompt the user if they would like to modify any of the attributes, then modify them if they do.
            Movie updatedMovie = modify(movie, scanner);
            System.out.println("The final movie is: \n" + updatedMovie);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static <T> T create(TesterMode mode, Scanner scanner) {
        // Prompt for all information pertaining to the content.
        String show_id = Reader.read(scanner, "Please enter a Show ID:");
        String title = Reader.read(scanner, "Please enter a Title:");
        String director = Reader.read(scanner, "Please enter a Director:");
        String country = Reader.read(scanner, "Please enter a Country:");
        int release_year = Reader.read(scanner, "Please enter a Release Year:", Integer::parseInt);
        int duration = Reader.read(scanner, mode == TesterMode.TV_SHOW
                ? "Please enter the number of seasons:"
                : "Please enter the duration in minutes:", Integer::parseInt);

        String genre = Reader.read(scanner, "Please enter a Genre:");

        // If the mode is a TV Show, prompt for a TV Rating, otherwise prompt for a Movie Rating - then return the created content.
        if (mode == TesterMode.TV_SHOW) {
            TvRating rating = Reader.read(scanner, "Please enter a TV Rating:", TvRating::fromString);
            return (T) new TvShow(show_id, title, director, country, release_year, rating, duration, genre);
        }

        MovieRating rating = Reader.read(scanner, "Please enter a MIPAA rating:", MovieRating::fromString);
        return (T) new Movie(show_id, title, director, country, release_year, rating, duration, genre);
    }

    private static <T> T modify(T object, Scanner scanner) {
        // Prompt the user if they would like to modify any of the attributes.
        boolean shouldModify = Reader.read(scanner, "Would you like to change any of the attributes?", val -> {
            if (val.equalsIgnoreCase("yes")) return true;
            if (val.equalsIgnoreCase("no")) return false;
            return Boolean.parseBoolean(val);
        });

        // If they do not want to modify any attributes, return the object.
        if (!shouldModify) {
            System.out.println("We will not modify any attributes, quitting.");
            return object;
        }

        // Prompt the user for the attribute they would like to modify, then read it.
        Field field = Reader.read(scanner, "Type the attribute you would like to change:", name -> {
            try {
                // Attempt to find the field in the super class, if found return it.
                Field superClass = object.getClass().getSuperclass().getDeclaredField(name);
                return superClass;
            } catch (NoSuchFieldException e) {
                // If the field is not found in the super class, continue.
            }

            try {
                // Attempt to find the field in the class, if found return it.
                Field subClass = object.getClass().getDeclaredField(name);
                return subClass;
            } catch (NoSuchFieldException e) {
                // If the field is not found in the class, throw an exception and send an error to the user.
                System.out.println("Attribute not found.");
                throw new RuntimeException("Attribute not found", e);
            }
        });

        // Set the field to be accessible, so it can be read + written to.
        field.setAccessible(true);

        try {
            // Print the old value of the field.
            System.out.println("Old value: " + field.get(object));
        } catch (IllegalAccessException e) {
            System.out.println("Could not read old value from field.");
        }

        // Prompt the user for the new value of the field, then set it.
        Object value = Reader.read(scanner, "What would you like to change " + field.getName() + " to?", val -> {
            Class<?> type = field.getType();
            if (type == String.class) return val;
            if (type == int.class) return Integer.parseInt(val);
            if (type == TvRating.class) return TvRating.fromString(val);
            if (type == MovieRating.class) return MovieRating.fromString(val);

            throw new RuntimeException("Unknown type: " + type);
        });

        try {
            // Set the new value of the field.
            field.set(object, value);
        } catch (IllegalAccessException e) {
            System.out.println("Could not write value to field.");
        }

        // Print the new object and return it.
        System.out.println(object);
        return object;
    }

}
