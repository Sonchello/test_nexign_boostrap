package com.example.demo.controller;

import com.example.demo.model.UdrReport;
import com.example.demo.service.UdrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для работы с UDR
 * Позволяет получать отчеты о звонках как для конкретного абонента, так и для всех абонентов
 */
@RestController
@RequestMapping("/udr")
public class UdrController {

    @Autowired
    private UdrService udrService;

    /**
     * Получение UDR-отчета для конкретного абонента
     * Можно указать месяц
     * @param msisdn номер абонента, для которого запрашивается отчет
     * @param month  месяц, за который формируется отчет
     */
    @GetMapping("/{msisdn}")
    public UdrReport getUdrReport(@PathVariable String msisdn,
                                  @RequestParam(required = false) Integer month) {
        return udrService.generateUdrReport(msisdn, month);
    }

    /**
     * Получение UDR-отчетов для всех абонентов
     * @param month месяц, за который формируется отчет
     * @return карта с номерами абонентов и их UDR-отчетами
     */
    @GetMapping("/all")
    public Map<String, UdrReport> getAllUdrReports(@RequestParam(required = false) Integer month) {
        return udrService.generateAllUdrReports(month);
    }
}
