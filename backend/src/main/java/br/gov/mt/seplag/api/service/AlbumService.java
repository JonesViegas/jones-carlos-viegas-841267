package br.gov.mt.seplag.api.service;

import br.gov.mt.seplag.api.model.Album;
import br.gov.mt.seplag.api.model.Artist;
import br.gov.mt.seplag.api.repository.AlbumRepository;
import br.gov.mt.seplag.api.repository.ArtistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final FileService fileService;

    // Construtor manual para injeção de dependência
    public AlbumService(AlbumRepository albumRepository, 
                        ArtistRepository artistRepository, 
                        FileService fileService) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.fileService = fileService;
    }

    /**
     * REQUISITO 6.3.1-g: Faz o upload para S3 e vincula o álbum ao artista (N:N)
     */
    @Transactional
    public Album createAlbum(String title, LocalDate releaseDate, MultipartFile cover, Long artistId) throws Exception {
        // 1. Faz o upload físico da imagem para o MinIO
        String fileName = fileService.uploadFile(cover);
        
        // 2. Busca o artista no banco (Obrigatório para o vínculo)
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artista de ID " + artistId + " não encontrado."));

        // 3. Cria e salva o novo Álbum
        Album album = new Album(title, releaseDate, fileName);
        Album savedAlbum = albumRepository.save(album);
        
        // 4. LÓGICA N:N: Adiciona o álbum à lista do artista e salva o artista
        // Isso fará o Hibernate inserir automaticamente na tabela 'artist_album'
        artist.getAlbums().add(savedAlbum);
        artistRepository.save(artist);
        
        return savedAlbum;
    }

    /**
     * REQUISITO 6.3.1-i: Gera link pré-assinado de 30 minutos para visualização segura
     */
    public String getCoverUrl(String fileName) throws Exception {
        return fileService.getPresignedUrl(fileName);
    }
}