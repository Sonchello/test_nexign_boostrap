package com.example.demo.repository;

import com.example.demo.model.CdrRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с CDR-записями, позволяет выполнять поиск записей о звонках в базе данных
 */
@Repository
public interface CdrRepository extends JpaRepository<CdrRecord, Long> {

    /**
     * Находит все CDR-записи, в которых указанный номер был исходящим
     * @param msisdn номер телефона абонента.
     * @return Список CDR-записей, где указанный номер является инициатором вызова
     */
    List<CdrRecord> findByFromMsisdn(String msisdn);

    /**
     * Находит все CDR-записи, в которых указанный номер был принимающим
     * @param msisdn номер телефона абонента
     * @return список CDR-записей, где указанный номер является получателем вызова
     */
    List<CdrRecord> findByToMsisdn(String msisdn);

    /**
     * Выполняет поиск всех вызовов, где абонент участвовал в качестве инициатора или получателя вызова в заданный период времени.
     * @param msisdn    номер телефона абонента
     * @param startDate начало временного диапазона поиска
     * @param endDate   конец временного диапазона поиска
     */
    @Query("SELECT c FROM CdrRecord c WHERE (c.fromMsisdn = :msisdn OR c.toMsisdn = :msisdn) AND c.startTime BETWEEN :startDate AND :endDate")
    List<CdrRecord> findCallsByMsisdnAndPeriod(
            @Param("msisdn") String msisdn,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
