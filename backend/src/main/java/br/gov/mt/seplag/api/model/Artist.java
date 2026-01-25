package br.gov.mt.seplag.api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String genre;

    @Column(name = "regional_id")
    private Integer regionalId;

    /**
     * REQUISITO DO EDITAL: Relacionamento N:N (Muitos-para-Muitos).
     * Um artista pode participar de vários álbuns e um álbum pode ter vários artistas.
     * O @JoinTable cria automaticamente a tabela de ligação 'artist_album'.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "artist_album",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    private List<Album> albums = new ArrayList<>();

    public Artist() {}

    public Artist(String name, String genre, Integer regionalId) {
        this.name = name;
        this.genre = genre;
        this.regionalId = regionalId;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public Integer getRegionalId() { return regionalId; }
    public void setRegionalId(Integer regionalId) { this.regionalId = regionalId; }
    public List<Album> getAlbums() { return albums; }
    public void setAlbums(List<Album> albums) { this.albums = albums; }
}