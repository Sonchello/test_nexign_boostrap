package com.example.demo.service;

import com.example.demo.model.Abonent;
import com.example.demo.model.CdrRecord;
import com.example.demo.repository.AbonentRepository;
import com.example.demo.repository.CdrRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CdrServiceTest {

    @Mock
    private CdrRepository cdrRepository;

    @Mock
    private AbonentRepository abonentRepository;

    @InjectMocks
    private CdrService cdrService;

    @Test
    public void testGenerateCdrRecords() {
        Abonent abonent = new Abonent();
        abonent.setMsisdn("79110000001");

        when(abonentRepository.findAll()).thenReturn(Collections.singletonList(abonent));

        // вызов тестируемого метода
        cdrService.generateCdrRecords();

        verify(cdrRepository, atLeastOnce()).save(any(CdrRecord.class));
    }

    @Test
    public void testGenerateCdrReport() {
        String msisdn = "79110000001";
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CdrRecord record = new CdrRecord();
        record.setFromMsisdn(msisdn);
        record.setToMsisdn("79110000002");
        record.setStartTime(startDate);
        record.setEndTime(endDate);

        when(cdrRepository.findCallsByMsisdnAndPeriod(msisdn, startDate, endDate))
                .thenReturn(Collections.singletonList(record));

        // вызов тестируемого метода
        UUID reportId = cdrService.generateCdrReport(msisdn, startDate, endDate);

        assertNotNull(reportId); // Проверка, что отчет создан и возвращен UUID
    }
}