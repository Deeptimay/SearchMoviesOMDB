package com.example.searchmoviesomdb.models;

import java.io.Serializable;
import java.util.Objects;

public class MovieDataSet implements Serializable {
    public String Title;
    public String Year;
    public String imdbID;
    public String Type;
    public String Poster;

    @Override
    public String toString() {
        return "MovieDataSet{" +
                "Title='" + Title + '\'' +
                ", Year='" + Year + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", Type='" + Type + '\'' +
                ", Poster='" + Poster + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieDataSet)) return false;
        MovieDataSet that = (MovieDataSet) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getYear(), that.getYear()) &&
                Objects.equals(getImdbID(), that.getImdbID()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getPoster(), that.getPoster());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getYear(), getImdbID(), getType(), getPoster());
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }
}
