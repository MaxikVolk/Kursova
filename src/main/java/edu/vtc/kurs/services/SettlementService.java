package edu.vtc.kurs.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.vtc.kurs.dto.SettlementDTO;
import edu.vtc.kurs.dto.SettlementInfoDTO;
import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.SettlementPhoto;
import edu.vtc.kurs.repositories.SettlementPhotoRepository;
import edu.vtc.kurs.repositories.SettlementRepository;
import edu.vtc.kurs.util.GoogleSearch;
import edu.vtc.kurs.util.SerpApiSearchException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SettlementService {
    private final SettlementRepository settlementRepository;

    private final SettlementPhotoRepository settlementPhotoRepository;
    private Settlement settlement;

    public SettlementService(SettlementRepository settlementRepository, SettlementPhotoRepository settlementPhotoRepository) {
        this.settlementRepository = settlementRepository;
        this.settlementPhotoRepository = settlementPhotoRepository;
    }

    public void save(SettlementDTO settlementDTO) {
        this.settlement = settlementRepository.save(map(settlementDTO));
        SettlementPhotoService settlementPhotoService = new SettlementPhotoService(settlementPhotoRepository,this);
        settlementPhotoService.start();
    }

    private Settlement map(SettlementDTO settlementDTO) {
        Settlement settlement = new Settlement();
        settlement.setName(settlementDTO.getName());
        settlement.setRegion(settlementDTO.getRegion());
        settlement.setDescription(settlementDTO.getDescription());
        return settlement;
    }

    public List<Settlement> findAll() {
        return settlementRepository.findAll();
    }

    public Settlement findById(long id) {
        return settlementRepository.findById(id).orElse(null);
    }

    public void deleteById(long id) {
        settlementRepository.deleteById(id);
    }

    @Transactional
    public void edit(long id, SettlementDTO settlementDTO) {
        Settlement settlement = settlementRepository.findById(id).orElseThrow();
        settlement.setRegion(settlementDTO.getRegion());
        settlement.setName(settlementDTO.getName());
        settlement.setDescription(settlementDTO.getDescription());
    }

    public List<Settlement> findAll(String sort) {
        List<Settlement> settlements = settlementRepository.findAll();
        switch (sort) {
            case "settlementName" -> settlements.sort(Comparator.comparing(Settlement::getName));
            case "regionName" -> settlements.sort(Comparator.comparing(Settlement::getRegion));
        }
        return settlements;
    }

    public List<Settlement> findSortedAndFiltered(String sort, String region) {
        List<Settlement> settlements;
        if (sort == null) {
            settlements = findAll();
        } else {
            settlements = findAll(sort);
        }
        if (region == null) {
            return settlements;
        } else {
            return settlements.stream().filter(settlement -> region.equals(settlement.getRegion()))
                    .collect(Collectors.toList());
        }
    }

    public SettlementInfoDTO getSettlement(long id) {
        Settlement settlement = settlementRepository.findById(id).orElseThrow();
        SettlementInfoDTO settlementInfoDTO = new SettlementInfoDTO();
        settlementInfoDTO.setDescription(settlement.getDescription());
        settlementInfoDTO.setName(settlement.getName());
        settlementInfoDTO.setRegion(settlement.getRegion());
        List<String> photos = new ArrayList<>();
        for(SettlementPhoto s: settlement.getSettlementPhotos()){
            photos.add(s.getPhotoUrl());
        }
        settlementInfoDTO.setPhotos(photos);
        return settlementInfoDTO;
    }

    public List<String> getPhotos(String settlement, String region) throws SerpApiSearchException {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("q","Beautiful "+settlement+", "+region+" region");
        parameter.put("gl", "ua");
        parameter.put("save", "active");
        parameter.put("filter", "0");
        parameter.put("tbm", "isch");
        parameter.put("ijn", "0");
        parameter.put("api_key", "c8392fe34248fc13ec1ff1dbe043498397710c423f23003ac5b266e7df9459ef");

        GoogleSearch search = new GoogleSearch(parameter);
        List<String> result = new ArrayList<>();
        JsonObject results = search.getJson();
        JsonArray images_results = results.getAsJsonArray("images_results");
        int count=0;
        for(JsonElement s : images_results){
            count++;
            JsonObject s1 = (JsonObject) s;
            result.add(s1.getAsJsonPrimitive("original").getAsString());
            if(count==5) return result;
        }
        return result;
    }

    public Settlement getSettlement() {
        return settlement;
    }
}
