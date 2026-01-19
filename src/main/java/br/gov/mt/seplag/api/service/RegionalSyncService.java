package br.gov.mt.seplag.api.service;

import br.gov.mt.seplag.api.model.Regional;
import br.gov.mt.seplag.api.repository.RegionalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@Slf4j

public class RegionalSyncService {
   
    private final RegionalRepository regionalRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "https://integrador-argus-api.geia.vip/v1/regionais";

    @Transactional
    public void syncRegionais() {
        log.info("Iniciando sincronização de regionais...");

        // 1. Buscar dados da API externa
        Regional[] externalData = restTemplate.getForObject(API_URL, Regional[].class);
        if (externalData == null) return;

        List<Regional> externalList = Arrays.asList(externalData);
        
        // 2. Buscar dados atuais do nosso banco (apenas os ativos)
        List<Regional> localActive = regionalRepository.findAllByAtivoTrue();
        Map<Integer, Regional> localMap = localActive.stream()
                .collect(Collectors.toMap(Regional::getId, r -> r));

        // 3. IDs que vieram na API
        Set<Integer> externalIds = externalList.stream().map(Regional::getId).collect(Collectors.toSet());

        // LOGICA DO EDITAL:
        for (Regional ext : externalList) {
            Regional loc = localMap.get(ext.getId());

            if (loc == null) {
                // 1) Novo no endpoint -> inserir
                ext.setAtivo(true);
                ext.setUltimaAtualizacao(LocalDateTime.now());
                regionalRepository.save(ext);
            } else if (!loc.getNome().equals(ext.getNome())) {
                // 3) Atributo alterado -> inativar antigo e criar novo
                loc.setAtivo(false);
                regionalRepository.save(loc);

                ext.setAtivo(true);
                ext.setUltimaAtualizacao(LocalDateTime.now());
                regionalRepository.save(ext);
            }
        }

        // 2) Ausente no endpoint -> inativar localmente
        for (Regional loc : localActive) {
            if (!externalIds.contains(loc.getId())) {
                loc.setAtivo(false);
                regionalRepository.save(loc);
            }
        }

        log.info("Sincronização concluída com sucesso.");
    }
}