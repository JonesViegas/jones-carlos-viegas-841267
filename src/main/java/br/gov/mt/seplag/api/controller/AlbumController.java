package br.gov.mt.seplag.api.controller;

import br.gov.mt.seplag.api.model.Album;
import br.gov.mt.seplag.api.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Album> create(
            @RequestParam("title") String title,
            @RequestParam("releaseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate,
            @RequestParam("cover") MultipartFile cover) throws Exception {
        
        return ResponseEntity.ok(albumService.createAlbum(title, releaseDate, cover));
    }

    @GetMapping("/{id}/cover")
    public ResponseEntity<String> getCoverUrl(@PathVariable Long id, @RequestParam("fileName") String fileName) throws Exception {
        return ResponseEntity.ok(albumService.getCoverUrl(fileName));
    }
}