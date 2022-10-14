package edu.vtc.kurs.services;

import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.SettlementPhoto;
import edu.vtc.kurs.repositories.SettlementPhotoRepository;
import org.springframework.stereotype.Service;
import serpapi.SerpApiSearchException;

/**
 * The type Settlement photo service.
 */
@Service
public class SettlementPhotoService extends Thread {
    private final SettlementPhotoRepository settlementPhotoRepository;

    private final SettlementService settlementService;

    /**
     * Instantiates a new Settlement photo service.
     *
     * @param settlementPhotoRepository the settlement photo repository
     * @param settlementService         the settlement service
     */
    public SettlementPhotoService(SettlementPhotoRepository settlementPhotoRepository, SettlementService settlementService) {
        this.settlementPhotoRepository = settlementPhotoRepository;
        this.settlementService = settlementService;
    }
    /**
     * Run another thread where we are saving settlement photos from google search
     */
    @Override
    public void run() {
        Settlement settlement = settlementService.getSettlement();
        try {
            for (String photo : settlementService.getPhotos(settlement.getName(), settlement.getRegion())) {
                SettlementPhoto settlementPhoto = new SettlementPhoto();
                settlementPhoto.setSettlement(settlement);
                settlementPhoto.setPhotoUrl(photo);
                settlementPhotoRepository.save(settlementPhoto);
            }
        } catch (SerpApiSearchException e) {
            e.printStackTrace();
        }
    }
}
