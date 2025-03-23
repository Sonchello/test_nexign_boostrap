package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import jakarta.persistence.Column;

/**
 * Сущность, представляющая CDR-запись
 * Хранит информацию о звонках абонентов
 */
@Entity
@Data
public class CdrRecord {

    /**
     * Уникальный идентификатор CDR-записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип вызова
     */
    private String callType;


    private String fromMsisdn;
    private String toMsisdn;

    /**
     * Время начала звонка, хранится в формате TIMESTAMP без дробной части секунд
     */
    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime startTime;

    /**
     * Время окончания звонка.
     */
    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime endTime;
}
