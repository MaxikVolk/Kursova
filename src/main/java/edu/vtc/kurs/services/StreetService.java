package edu.vtc.kurs.services;

import edu.vtc.kurs.dto.StreetDTO;
import edu.vtc.kurs.models.Street;
import edu.vtc.kurs.repositories.StreetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Street service.
 */
@Service
public class StreetService {
    private final StreetRepository streetRepository;

    /**
     * Instantiates a new Street service.
     *
     * @param streetRepository the street repository
     */
    public StreetService(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Street> findAll() {
        return streetRepository.findAll();
    }

    /**
     * Save.
     *
     * @param streetDTO the street dto
     */
    public void save(StreetDTO streetDTO) {
        streetRepository.save(map(streetDTO));
    }

    private Street map(StreetDTO streetDTO) {
        Street street = new Street();
        street.setSettlement(streetDTO.getSettlement());
        street.setName(streetDTO.getName());
        return street;
    }

    /**
     * Find street by id.
     *
     * @param id the id
     * @return the street
     */
    public Street findById(long id) {
        return streetRepository.findById(id).orElseThrow();
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteById(long id) {
        streetRepository.deleteById(id);
    }

    /**
     * Edit.
     *
     * @param id        the id
     * @param streetDTO the street dto
     */
    @Transactional
    public void edit(long id, StreetDTO streetDTO) {
        Street street = streetRepository.findById(id).orElseThrow();
        street.setName(streetDTO.getName());
        street.setSettlement(streetDTO.getSettlement());
    }

    /**
     * Find list of Streets
     *
     * @param sort the sort
     * @return the list
     */
    public List<Street> findAll(String sort) {
        List<Street> streets = streetRepository.findAll();
        switch (sort) {
            case "settlementName" -> streets.sort(Comparator.comparing(a -> a.getSettlement().getName()));
            case "regionName" -> streets.sort(Comparator.comparing(a -> a.getSettlement().getRegion()));
            case "streetName" -> streets.sort(Comparator.comparing(Street::getName));
        }
        return streets;
    }

    /**
     * Find sorted and filtered list.
     *
     * @param sort       the sort
     * @param region     the region
     * @param settlement the settlement
     * @return list of Streets
     */
    public List<Street> findSortedAndFiltered(String sort, String region, String settlement) {
        List<Street> streets;
        if (sort == null) {
            streets = findAll();
        } else {
            streets = findAll(sort);
        }
        if (region != null) {
            streets = streets.stream().filter(street ->
                    region.equals(street.getSettlement().getRegion())).collect(Collectors.toList());
        }
        if (settlement != null && !settlement.isBlank()) {
            streets = streets.stream().filter(street ->
                    street.getSettlement().getName().startsWith(settlement)).collect(Collectors.toList());
        }
        return streets;
    }
}
