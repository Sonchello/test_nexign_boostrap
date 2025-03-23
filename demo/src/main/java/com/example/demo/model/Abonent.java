package com.example.demo.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Сущность, представляющая абонента
 * Хранит информацию о номере телефона
 */
@Entity
@Data
public class Abonent {

    /**
     * Уникальный идентификатор абонента, генерируется автоматически
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Номер телефона абонента
     */
    private String msisdn;
}
