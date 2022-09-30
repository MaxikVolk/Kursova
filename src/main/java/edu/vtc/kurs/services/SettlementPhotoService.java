package edu.vtc.kurs.services;

import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.SettlementPhoto;
import edu.vtc.kurs.repositories.SettlementPhotoRepository;
import edu.vtc.kurs.util.SerpApiSearchException;
import org.springframework.stereotype.Service;

@Service
public class SettlementPhotoService extends Thread {
    private final SettlementPhotoRepository settlementPhotoRepository;

    private final SettlementService settlementService;

    public SettlementPhotoService(SettlementPhotoRepository settlementPhotoRepository, SettlementService settlementService) {
        this.settlementPhotoRepository = settlementPhotoRepository;
        this.settlementService = settlementService;
    }

    public void run(){
        Settlement settlement = settlementService.getSettlement();
        try {
            for(String photo:settlementService.getPhotos(settlement.getName(),settlement.getRegion())){
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
