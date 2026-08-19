package com.example.goldenticketnew.cgvadmin.dto.response;

import com.example.goldenticketnew.model.Movie;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CgvMovieDTO {
    private int id;
    private String name;
    private String smallImageURl;
    private String shortDescription;
    private String longDescription;
    private String largeImageURL;
    private String director;
    private String actors;
    private String categories;
    private LocalDate releaseDate;
    private int duration;
    private String trailerURL;
    private String language;
    private String rated;
    private int isShowing;

    public CgvMovieDTO(Movie movie) {
        this.id = movie.getId();
        this.name = movie.getName();
        this.smallImageURl = movie.getSmallImageURl();
        this.shortDescription = movie.getShortDescription();
        this.longDescription = movie.getLongDescription();
        this.largeImageURL = movie.getLargeImageURL();
        this.director = movie.getDirector();
        this.actors = movie.getActors();
        this.categories = movie.getCategories();
        this.releaseDate = movie.getReleaseDate();
        this.duration = movie.getDuration();
        this.trailerURL = movie.getTrailerURL();
        this.language = movie.getLanguage();
        this.rated = movie.getRated();
        this.isShowing = movie.getIsShowing();
    }
}
