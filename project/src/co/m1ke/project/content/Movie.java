package co.m1ke.project.content;

import co.m1ke.project.rating.MovieRating;

public class Movie extends Watchable {

    // Instantiate the fields for a movie, which are the MIPAA rating, and duration.
    private MovieRating rating;
    private int duration;

    // Create a constructor for a movie, which takes in all the fields from the Watchable class, and the fields from the Movie class.
    public Movie(String show_id, String title, String director, String country, int release_year, MovieRating rating, int duration, String genre) {
        super(show_id, title, director, country, release_year, genre);
        this.rating = rating;
        this.duration = duration;
    }

    /**
     * Get the rating of the movie.
     * @return The rating of the movie.
     */
    public MovieRating getRating() {
        return rating;
    }

    /**
     * Set the rating of the movie.
     * @param rating The rating of the movie.
     */
    public void setRating(MovieRating rating) {
        this.rating = rating;
    }

    /**
     * Get the duration of the movie.
     * @return The duration of the movie.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Set the duration of the movie.
     * @param duration The duration of the movie.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        // Return a formatted string with all the information about the movie.
        String title = this.getTitle().contains(",")
                ? "\"" + this.getTitle() + "\""
                : this.getTitle();

        String director = this.getDirector().contains(",")
                ? "\"" + this.getDirector() + "\""
                : this.getDirector();

        String country = this.getCountry().contains(",")
                ? "\"" + this.getCountry() + "\""
                : this.getCountry();

        String genre = this.getGenre().contains(",")
                ? "\"" + this.getGenre() + "\""
                : this.getGenre();

        return String.format(String.join(",",
                this.getShowId(),
                "Movie",
                title,
                director,
                country,
                String.valueOf(this.getReleaseYear()),
                this.getRating().getName(),
                this.getDuration() + " min",
                genre));
    }

}
