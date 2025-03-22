package com.example.demo.repository;

import com.example.demo.model.CdrRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CdrRepository extends JpaRepository<CdrRecord, Long> {
    List<CdrRecord> findByFromMsisdn(String msisdn);
    List<CdrRecord> findByToMsisdn(String msisdn);

    @Query("SELECT c FROM CdrRecord c WHERE (c.fromMsisdn = :msisdn1 OR c.toMsisdn = :msisdn2) AND c.startTime BETWEEN :startDate AND :endDate")
    List<CdrRecord> findByFromMsisdnOrToMsisdnAndStartTimeBetween(@Param("msisdn1") String msisdn1,
                                                                  @Param("msisdn2") String msisdn2,
                                                                  @Param("startDate") LocalDateTime startDate,
                                                                  @Param("endDate") LocalDateTime endDate);
}