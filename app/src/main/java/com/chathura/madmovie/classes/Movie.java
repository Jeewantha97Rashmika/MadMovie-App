package com.chathura.madmovie.classes;

public class Movie implements Comparable<Movie>{

    private String title;
    private String imdbID;
    private String year;
    private String type;
    private String poster;

    private String genre;
    private String actors;
    private String plot;
    private double imdbRating;
    private String runtime;
    private String director;
    private  String country;

    public Movie(){}

    public Movie(String title, String imdbID, String year, String type, String poster) {
        this.title = title;
        this.imdbID = imdbID;
        this.year = year;
        this.type = type;
        this.poster = poster;
    }

    public Movie(String title, String imdbID, String year, String type, String poster, String genre, String actors, String plot, double imdbRating, String runtime, String director, String country) {
        this.title = title;
        this.imdbID = imdbID;
        this.year = year;
        this.type = type;
        this.poster = poster;
        this.genre = genre;
        this.actors = actors;
        this.plot = plot;
        this.imdbRating = imdbRating;
        this.runtime = runtime;
        this.director = director;
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", year='" + year + '\'' +
                ", type='" + type + '\'' +
                ", poster='" + poster + '\'' +
                ", genre='" + genre + '\'' +
                ", actors='" + actors + '\'' +
                ", plot='" + plot + '\'' +
                ", imdbRating=" + imdbRating +
                ", runtime='" + runtime + '\'' +
                ", director='" + director + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public int compareTo(Movie o) {
        return Integer.parseInt(String.valueOf(this.year))-Integer.parseInt(String.valueOf(o.year));
    }
}
