package br.gov.mt.seplag.api.service;

import br.gov.mt.seplag.api.dto.ArtistDTO;
import br.gov.mt.seplag.api.model.Artist;
import br.gov.mt.seplag.api.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Transactional
    public Artist save(ArtistDTO dto) {
        Artist artist = new Artist(dto.getName(), dto.getGenre());
        
        // Aqui depois adicionaremos a lógica para vincular álbuns
        return artistRepository.save(artist);
    }

    public Artist findById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));
    }
}
