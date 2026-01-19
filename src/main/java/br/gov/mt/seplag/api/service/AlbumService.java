package br.gov.mt.seplag.api.service;

import br.gov.mt.seplag.api.model.Album;
import br.gov.mt.seplag.api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final FileService fileService;
    private final SimpMessagingTemplate messagingTemplate; 

@Transactional
public Album createAlbum(String title, LocalDate releaseDate, MultipartFile cover) throws Exception {
    String fileName = fileService.uploadFile(cover);
    Album album = new Album(title, releaseDate, fileName);
    Album saved = albumRepository.save(album);
    
    // NOTIFICAÇÃO EM TEMPO REAL (Requisito Sênior)
    messagingTemplate.convertAndSend("/topic/new-album", "Novo album cadastrado: " + saved.getTitle());
    
    return saved;
}

    public String getCoverUrl(String fileName) throws Exception {
        return fileService.getPresignedUrl(fileName);
    }
}