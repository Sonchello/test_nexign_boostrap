package com.example.demo.controller;

import com.example.demo.service.CdrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/cdr")
public class CdrController {

    @Autowired
    private CdrService cdrService;

    @PostMapping("/generate")
    public String generateCdrRecords() {
        cdrService.generateCdrRecords();
        return "CDR записи успешно сгенерированы!";
    }

    @PostMapping("/report")
    public String generateCdrReport(@RequestParam String msisdn,
                                    @RequestParam LocalDateTime startDate,
                                    @RequestParam LocalDateTime endDate) {
        UUID reportId = cdrService.generateCdrReport(msisdn, startDate, endDate);
        return "CDR отчет будет сгенерирован. ID запроса: " + reportId;
    }
}