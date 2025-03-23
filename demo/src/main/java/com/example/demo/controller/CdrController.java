package com.example.demo.controller;
import com.example.demo.service.CdrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Контроллер для работы с CDR
 * Здесь генерируются CDR-записи и формируется отчет
 */
@RestController
@RequestMapping("/cdr")
public class CdrController {

    @Autowired
    private CdrService cdrService;

    /**
     * Генерация случайных CDR-записей
     * @return строка с подтверждением генерации
     */
    @PostMapping("/generate")
    public String generateCdrRecords() {
        cdrService.generateCdrRecords();
        return "CDR записи успешно сгенерированы!";
    }

    /**
     * Генерация отчета по звонкам для указанного абонента
     * Передается номер абонента и временной диапазон
     * @param msisdn     номер абонента
     * @param startDate  начало периода
     * @param endDate    конец периода
     * @return ID запроса на генерацию отчета
     */
    @PostMapping("/report")
    public String generateCdrReport(@RequestParam String msisdn,
                                    @RequestParam LocalDateTime startDate,
                                    @RequestParam LocalDateTime endDate) {
        UUID reportId = cdrService.generateCdrReport(msisdn, startDate, endDate);
        return "CDR отчет будет сгенерирован. ID запроса: " + reportId;
    }
}
