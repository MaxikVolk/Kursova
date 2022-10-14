package edu.vtc.kurs.repositories;

import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.Street;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Street repository.
 */
public interface StreetRepository extends JpaRepository<Street, Long> {
    /**
     * Find street by name and settlement street.
     *
     * @param street     the street
     * @param settlement the settlement
     * @return the street
     */
    Street findStreetByNameAndSettlement(String street, Settlement settlement);
}
