package co.m1ke.project.content;

public class Watchable {

    // Instantiate the fields for a watchable, which are the show ID, title, director, country, release year, and genre.
    private String show_id;
    private String title;
    private String director;
    private String country;
    private int release_year;
    private String genre;

    // Create a constructor for a watchable, which takes in all the fields from the Watchable class.
    public Watchable(String show_id, String title, String director, String country, int release_year, String genre) {
        this.show_id = show_id;
        this.title = title;
        this.director = director;
        this.country = country;
        this.release_year = release_year;
        this.genre = genre;
    }

    /**
     * Get the show ID of the watchable.
     * @return The show ID of the watchable.
     */
    public String getShowId() {
        return show_id;
    }

    /**
     * Set the show ID of the watchable.
     * @param show_id The show ID of the watchable.
     */
    public void setShowId(String show_id) {
        this.show_id = show_id;
    }

    /**
     * Get the title of the watchable.
     * @return The title of the watchable.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the watchable.
     * @param title The title of the watchable.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the director of the watchable.
     * @return The director of the watchable.
     */
    public String getDirector() {
        return director;
    }

    /**
     * Set the director of the watchable.
     * @param director The director of the watchable.
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Get the country of the watchable.
     * @return The country of the watchable.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the country of the watchable.
     * @param country The country of the watchable.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get the release year of the watchable.
     * @return The release year of the watchable.
     */
    public int getReleaseYear() {
        return release_year;
    }

    /**
     * Set the release year of the watchable.
     * @param release_year The release year of the watchable.
     */
    public void setReleaseYear(int release_year) {
        this.release_year = release_year;
    }

    /**
     * Get the genre of the watchable.
     * @return The genre of the watchable.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Set the genre of the watchable.
     * @param genre The genre of the watchable.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        // Return the watchable as a string.
        return "Watchable{" +
                "show_id='" + show_id + '\'' +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", country='" + country + '\'' +
                ", release_year=" + release_year +
                ", genre='" + genre + '\'' +
                '}';
    }
}
