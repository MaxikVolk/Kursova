package edu.vtc.kurs.repositories;

import edu.vtc.kurs.models.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement,Long> {
}
