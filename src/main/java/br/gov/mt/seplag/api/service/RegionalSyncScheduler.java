package br.gov.mt.seplag.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegionalSyncScheduler {

    private final RegionalSyncService syncService;

    // Roda a cada 1 hora (3600000 ms)
    @Scheduled(fixedRate = 3600000)
    public void executeSync() {
        syncService.syncRegionais();
    }
}