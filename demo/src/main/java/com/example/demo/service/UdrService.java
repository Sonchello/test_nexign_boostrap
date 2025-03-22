package com.example.demo.service;

import com.example.demo.model.CdrRecord;
import com.example.demo.model.UdrReport;
import com.example.demo.repository.CdrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UdrService {

    @Autowired
    private CdrRepository cdrRepository;

    public UdrReport generateUdrReport(String msisdn, Integer month) {
        List<CdrRecord> incomingCalls = filterCallsByMonth(cdrRepository.findByToMsisdn(msisdn), month);
        List<CdrRecord> outcomingCalls = filterCallsByMonth(cdrRepository.findByFromMsisdn(msisdn), month);

        long incomingTotalTime = incomingCalls.stream()
                .mapToLong(cdr -> Duration.between(cdr.getStartTime(), cdr.getEndTime()).getSeconds())
                .sum();
        long outcomingTotalTime = outcomingCalls.stream()
                .mapToLong(cdr -> Duration.between(cdr.getStartTime(), cdr.getEndTime()).getSeconds())
                .sum();

        UdrReport udrReport = new UdrReport();
        udrReport.setMsisdn(msisdn);

        UdrReport.CallDetail incomingCallDetail = new UdrReport.CallDetail();
        incomingCallDetail.setTotalTime(formatDuration(incomingTotalTime));
        udrReport.setIncomingCall(incomingCallDetail);

        UdrReport.CallDetail outcomingCallDetail = new UdrReport.CallDetail();
        outcomingCallDetail.setTotalTime(formatDuration(outcomingTotalTime));
        udrReport.setOutcomingCall(outcomingCallDetail);

        return udrReport;
    }
    public Map<String, UdrReport> generateAllUdrReports(Integer month) {
        List<CdrRecord> allCalls = month != null ?
                cdrRepository.findAll().stream()
                        .filter(cdr -> cdr.getStartTime().getMonthValue() == month)
                        .collect(Collectors.toList()) :
                cdrRepository.findAll();

        Map<String, UdrReport> reports = new HashMap<>();

        allCalls.forEach(cdr -> {
            // Обработка исходящих звонков
            UdrReport callerReport = reports.computeIfAbsent(cdr.getFromMsisdn(), k -> {
                UdrReport report = new UdrReport();
                report.setMsisdn(cdr.getFromMsisdn());
                report.setOutcomingCall(new UdrReport.CallDetail());
                report.setIncomingCall(new UdrReport.CallDetail());
                return report;
            });
            long outcomingDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime()).getSeconds();
            long currentOutcomingTime = parseDuration(callerReport.getOutcomingCall().getTotalTime());
            callerReport.getOutcomingCall().setTotalTime(formatDuration(currentOutcomingTime + outcomingDuration));

            // Обработка входящих звонков
            UdrReport receiverReport = reports.computeIfAbsent(cdr.getToMsisdn(), k -> {
                UdrReport report = new UdrReport();
                report.setMsisdn(cdr.getToMsisdn());
                report.setOutcomingCall(new UdrReport.CallDetail());
                report.setIncomingCall(new UdrReport.CallDetail());
                return report;
            });
            long incomingDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime()).getSeconds();
            long currentIncomingTime = parseDuration(receiverReport.getIncomingCall().getTotalTime());
            receiverReport.getIncomingCall().setTotalTime(formatDuration(currentIncomingTime + incomingDuration));
        });

        return reports;
    }

    private List<CdrRecord> filterCallsByMonth(List<CdrRecord> calls, Integer month) {
        if (month == null) {
            return calls;
        }
        return calls.stream()
                .filter(cdr -> cdr.getStartTime().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    private long parseDuration(String duration) {
        if (duration == null || duration.isEmpty()) {
            return 0;
        }
        String[] parts = duration.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return hours * 3600 + minutes * 60 + seconds;
    }
}