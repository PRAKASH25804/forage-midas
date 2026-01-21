package com.jpmc.midascore.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jpmc.midascore.entity.IncentiveRecord;

@Repository
public interface IncentiveRepository extends JpaRepository<IncentiveRecord, Long> {
}
