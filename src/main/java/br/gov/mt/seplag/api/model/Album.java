package br.gov.mt.seplag.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private String coverUrl;

    public Album() {}
    public Album(String title, LocalDate releaseDate, String coverUrl) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.coverUrl = coverUrl;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}