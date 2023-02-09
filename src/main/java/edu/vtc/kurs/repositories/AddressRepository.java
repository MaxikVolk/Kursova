package edu.vtc.kurs.repositories;

import edu.vtc.kurs.models.Address;
import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Address repository.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
    /**
     * Exists by settlement and street and house number and flat number boolean.
     *
     * @param settlement  the settlement
     * @param street      the street
     * @param houseNumber the house number
     * @param FlatNumber  the flat number
     * @return the boolean
     */
    boolean existsBySettlementAndStreetAndHouseNumberAndFlatNumber
    (Settlement settlement, Street street, int houseNumber, int FlatNumber);
}
