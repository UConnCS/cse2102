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
        // Return a formatted string with all the information about the TV show.
        return String.format("TV Show ID: %s\nTitle: %s\nDirector: %s\nCountry: %s\nRelease Year: %d\nRating: %s\nSeasons: %d\nGenre: %s",
                this.getShowId(), this.getTitle(), this.getDirector(), this.getCountry(), this.getReleaseYear(), rating, seasons, this.getGenre());
    }
}
