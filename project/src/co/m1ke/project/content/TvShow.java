package co.m1ke.project.content;

import co.m1ke.project.rating.TvRating;

public class TvShow extends Watchable {

    // Instantiate the fields for a TV show, which are the TV rating, and seasons.
    private TvRating rating;
    private int seasons;

    // Create a constructor for a TV show, which takes in all the fields from the Watchable class, and the fields from the TV show class.
    public TvShow(String show_id, String title, String director, String country, int release_year, TvRating rating, int seasons, String genre) {
        super(show_id, title, director, country, release_year, genre);
        this.rating = rating;
        this.seasons = seasons;
    }

    /**
     * Get the rating of the TV show.
     * @return The rating of the TV show.
     */
    public TvRating getRating() {
        return rating;
    }

    /**
     * Set the rating of the TV show.
     * @param rating The rating of the TV show.
     */
    public void setRating(TvRating rating) {
        this.rating = rating;
    }

    /**
     * Get the seasons of the TV show.
     * @return The seasons of the TV show.
     */
    public int getSeasons() {
        return seasons;
    }

    /**
     * Set the seasons of the TV show.
     * @param seasons The seasons of the TV show.
     */
    public void setSeasons(int seasons) {
        this.seasons = seasons;
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
                "TV Show",
                title,
                director,
                country,
                String.valueOf(this.getReleaseYear()),
                this.getRating().getName(),
                this.getSeasons() + " Seasons",
                genre));
    }

}
