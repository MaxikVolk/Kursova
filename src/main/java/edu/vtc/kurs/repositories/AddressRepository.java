package edu.vtc.kurs.repositories;

import edu.vtc.kurs.models.Address;
import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.Street;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
    boolean existsBySettlementAndStreetAndHouseNumberAndFlatNumber
            (Settlement settlement, Street street, int houseNumber,int FlatNumber);
}
