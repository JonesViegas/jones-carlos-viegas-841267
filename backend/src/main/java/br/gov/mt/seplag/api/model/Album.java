package br.gov.mt.seplag.api.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "cover_url", length = 512)
    private String coverUrl;

    /**
     * RELACIONAMENTO N:N (Requisito do Edital)
     * O 'mappedBy' indica que a classe Artist é a "dona" do mapeamento.
     * @JsonIgnore evita que o sistema entre em loop infinito ao gerar o JSON.
     */
    @ManyToMany(mappedBy = "albums")
    @JsonIgnore
    private List<Artist> artists = new ArrayList<>();

    public Album() {}

    public Album(String title, LocalDate releaseDate, String coverUrl) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.coverUrl = coverUrl;
    }

    // Getters e Setters Manuais (Sem dependência de Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public List<Artist> getArtists() { return artists; }
    public void setArtists(List<Artist> artists) { this.artists = artists; }
}