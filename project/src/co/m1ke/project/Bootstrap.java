package co.m1ke.project;

import co.m1ke.project.content.Movie;
import co.m1ke.project.content.TvShow;
import co.m1ke.project.parser.CsvParser;
import co.m1ke.project.storage.StorageCluster;

import java.util.List;

public class Bootstrap {

    public static void main(String[] args) {
        List<String> lines = CsvParser.readLines("netflix.csv");
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
    }

}
