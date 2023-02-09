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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serpapi.GoogleSearch;
import serpapi.SerpApiSearchException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Settlement service.
 */
@Service
public class SettlementService {
    private final SettlementRepository settlementRepository;

    private final SettlementPhotoRepository settlementPhotoRepository;
    private Settlement settlement;

    /**
     * Instantiates a new Settlement service.
     *
     * @param settlementRepository      the settlement repository
     * @param settlementPhotoRepository the settlement photo repository
     */
    public SettlementService(SettlementRepository settlementRepository, SettlementPhotoRepository settlementPhotoRepository) {
        this.settlementRepository = settlementRepository;
        this.settlementPhotoRepository = settlementPhotoRepository;
    }

    private static Map<String, String> setParams(String settlement, String region) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("q", "Beautiful " + settlement + ", " + region + " region");
        parameter.put("gl", "ua");
        parameter.put("save", "active");
        parameter.put("filter", "0");
        parameter.put("tbm", "isch");
        parameter.put("num", "5");
        parameter.put("source", "java");
        parameter.put("api_key", "f355947ef488f131f8ad83d1a36d9a3a353c4cd8bdb8df29ed70017b7a8057b8");
        parameter.put("engine", "google");
        parameter.put("output", "json");
        return parameter;
    }

    /**
     * Save.
     *
     * @param settlementDTO the settlement dto
     */
    public void save(SettlementDTO settlementDTO) {
        this.settlement = settlementRepository.save(map(settlementDTO));
        SettlementPhotoService settlementPhotoService = new SettlementPhotoService(settlementPhotoRepository, this);
        settlementPhotoService.start();
    }

    private Settlement map(SettlementDTO settlementDTO) {
        Settlement settlement = new Settlement();
        settlement.setName(settlementDTO.getName());
        settlement.setRegion(settlementDTO.getRegion());
        settlement.setDescription(settlementDTO.getDescription());
        return settlement;
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Settlement> findAll() {
        return settlementRepository.findAll();
    }

    /**
     * Find settlement by id.
     *
     * @param id the id
     * @return the settlement
     */
    public Settlement findById(long id) {
        return settlementRepository.findById(id).orElse(null);
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteById(long id) {
        settlementRepository.deleteById(id);
    }

    /**
     * Edit.
     *
     * @param id            the id
     * @param settlementDTO the settlement dto
     */
    @Transactional
    public void edit(long id, SettlementDTO settlementDTO) {
        Settlement settlement = settlementRepository.findById(id).orElseThrow();
        settlement.setRegion(settlementDTO.getRegion());
        settlement.setName(settlementDTO.getName());
        settlement.setDescription(settlementDTO.getDescription());
    }

    /**
     * Find all list.
     *
     * @param sort the sort
     * @return list of Settlements
     */
    public List<Settlement> findAll(String sort) {
        List<Settlement> settlements = settlementRepository.findAll();
        switch (sort) {
            case "settlementName" -> settlements.sort(Comparator.comparing(Settlement::getName));
            case "regionName" -> settlements.sort(Comparator.comparing(Settlement::getRegion));
        }
        return settlements;
    }

    /**
     * Find sorted and filtered list.
     *
     * @param sort   the sort
     * @param region the region
     * @return list of Settlements
     */
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

    /**
     * Get settlement by id.
     *
     * @param id the id
     * @return the settlement
     */
    public SettlementInfoDTO getSettlement(long id) {
        Settlement settlement = settlementRepository.findById(id).orElseThrow();
        SettlementInfoDTO settlementInfoDTO = new SettlementInfoDTO();
        settlementInfoDTO.setDescription(settlement.getDescription());
        settlementInfoDTO.setName(settlement.getName());
        settlementInfoDTO.setRegion(settlement.getRegion());
        List<String> photos = new ArrayList<>();
        for (SettlementPhoto s : settlement.getSettlementPhotos()) {
            photos.add(s.getPhotoUrl());
        }
        settlementInfoDTO.setPhotos(photos);
        return settlementInfoDTO;
    }

    /**
     * Get photos by id.
     *
     * @param settlement the settlement
     * @param region     the region
     * @return list of photos links
     * @throws SerpApiSearchException the serp api search exception
     */
    public List<String> getPhotos(String settlement, String region) throws SerpApiSearchException {
        GoogleSearch search = new GoogleSearch(setParams(settlement, region));
        List<String> links = new ArrayList<>();
        JsonObject results = search.getJson();
        JsonArray images_results = results.getAsJsonArray("images_results");
        int count = 0;
        for (JsonElement s : images_results) {
            count++;
            JsonObject s1 = (JsonObject) s;
            links.add(s1.getAsJsonPrimitive("original").getAsString());
            if (count == 5) return links;
        }
        return links;
    }

    /**
     * Gets settlement.
     *
     * @return the settlement
     */
    public Settlement getSettlement() {
        return settlement;
    }
}
