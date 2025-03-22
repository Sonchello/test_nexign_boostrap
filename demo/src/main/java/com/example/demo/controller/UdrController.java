package com.example.demo.controller;

import com.example.demo.model.UdrReport;
import com.example.demo.service.UdrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/udr")
public class UdrController {

    @Autowired
    private UdrService udrService;

    @GetMapping("/{msisdn}")
    public UdrReport getUdrReport(@PathVariable String msisdn,
                                  @RequestParam(required = false) Integer month) {
        return udrService.generateUdrReport(msisdn, month);
    }

    @GetMapping("/all")
    public Map<String, UdrReport> getAllUdrReports(@RequestParam(required = false) Integer month) {
        return udrService.generateAllUdrReports(month);
    }
}