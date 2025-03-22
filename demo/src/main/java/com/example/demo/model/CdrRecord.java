package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.Column;

@Entity
@Data
public class CdrRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String callType;
    private String fromMsisdn;
    private String toMsisdn;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime endTime;
}