package edu.vtc.kurs.repositories;

import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.Street;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreetRepository extends JpaRepository<Street,Long> {
    Street findStreetByName(String name);

    Street findStreetByNameAndSettlement(String street, Settlement settlement);
}
