package hw5;

public class DocumentarySeries extends Documentary {
    
    // Instantiate instance variable, which in this case is the number of episodes
    private int episodes;
    private int seasons;

    /**
     * Constructor for the DocumentarySeries class, takes in a name, MipaaRating, TvRating, and number of episodes.
     * 
     * @param name The name of the movie
     * @param rating The MipaaRating of the movie
     * @param tvRating The TvRating of the movie
     * @param episodes The number of episodes in the series
     */
    public DocumentarySeries(String name, MipaaRating rating, TvRating tvRating, int episodes, int seasons) {
        super(name, rating, tvRating);
        this.episodes = episodes;
        this.seasons = seasons;
    }

    /**
     * Returns the number of episodes in the series
     * @return The number of episodes in the series
     */
    public int getEpisodes() {
        return episodes;
    }

    /**
     * Returns the number of seasons in the series
     * @return The number of seasons in the series
     */
    public int getSeasons() {
        return seasons;
    }

    @Override
    public void writeOutput() {
        System.out.println("Documentary Series: " + getName());
        System.out.println("MIPAA Rating: " + getRating());
        System.out.println("TV Rating: " + getTvRating());
        System.out.println("Episodes: " + getEpisodes());
        System.out.println("Seasons: " + getSeasons());
    }

}
