package br.gov.mt.seplag.api.controller;

import br.gov.mt.seplag.api.model.Album;
import br.gov.mt.seplag.api.service.AlbumService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/albums")
@CrossOrigin(origins = "*")
public class AlbumController {

    private final AlbumService albumService;

    // Construtor manual para evitar erros de compilação do Lombok
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    /**
     * REQUISITO 6.3.1-g: Upload de arquivos de capa de álbum.
     * O arquivo é enviado via MultipartForm para ser armazenado no MinIO (S3).
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Album> create(
            @RequestParam("title") String title,
            @RequestParam("artistId") Long artistId,
            @RequestParam("cover") MultipartFile cover) throws Exception {
        
        // Agora passamos o artistId para o service
        Album savedAlbum = albumService.createAlbum(title, LocalDate.now(), cover, artistId);
        return ResponseEntity.ok(savedAlbum);
}

    /**
     * REQUISITO 6.3.1-i: Recuperação de links pré-assinados com expiração de 30 minutos.
     * Este endpoint gera a URL segura para o Frontend exibir a imagem direto do S3.
     */
   @GetMapping("/{id}/cover-url")
    public ResponseEntity<java.util.Map<String, String>> getCoverPresignedUrl(
            @PathVariable Long id, 
            @RequestParam("fileName") String fileName) throws Exception {
        
        String url = albumService.getCoverUrl(fileName);
        
        // Retornamos um objeto JSON { "url": "http://..." }
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("url", url);
        
        return ResponseEntity.ok(response);
    }
}

