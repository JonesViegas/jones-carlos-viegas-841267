package br.gov.mt.seplag.api.controller;

import br.gov.mt.seplag.api.dto.ArtistDTO;
import br.gov.mt.seplag.api.model.Artist;
import br.gov.mt.seplag.api.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    public ResponseEntity<List<Artist>> getAll() {
        return ResponseEntity.ok(artistService.findAll());
    }

    @PostMapping
    public ResponseEntity<Artist> create(@RequestBody @Valid ArtistDTO dto) {
        return ResponseEntity.ok(artistService.save(dto));
    }
}