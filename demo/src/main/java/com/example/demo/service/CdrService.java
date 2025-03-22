package com.example.demo.service;

import com.example.demo.model.Abonent;
import com.example.demo.model.CdrRecord;
import com.example.demo.repository.AbonentRepository;
import com.example.demo.repository.CdrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class CdrService {

    @Autowired
    private CdrRepository cdrRepository;

    @Autowired
    private AbonentRepository abonentRepository;

    private static final int DAYS_IN_YEAR = 365;
    private static final int MINUTES_IN_DAY = 1440;
    private static final int MAX_CALL_DURATION_MINUTES = 60;
    private static final int MAX_CALLS_PER_DAY = 10;

    public void generateCdrRecords() {
        List<Abonent> abonents = abonentRepository.findAll();
        Random random = new Random();
        LocalDateTime startDate = LocalDateTime.now().minusYears(1).truncatedTo(ChronoUnit.SECONDS); // Обрезаем микросекунды

        for (int day = 0; day < DAYS_IN_YEAR; day++) {
            for (Abonent caller : abonents) {
                int callCount = random.nextInt(MAX_CALLS_PER_DAY);

                for (int call = 0; call < callCount; call++) {
                    Abonent receiver;
                    do {
                        receiver = abonents.get(random.nextInt(abonents.size()));
                    } while (receiver.getMsisdn().equals(caller.getMsisdn())); // исключение вызова самому себе

                    CdrRecord cdrRecord = new CdrRecord();
                    cdrRecord.setCallType(random.nextBoolean() ? "01" : "02");
                    cdrRecord.setFromMsisdn(caller.getMsisdn());
                    cdrRecord.setToMsisdn(receiver.getMsisdn());

                    LocalDateTime startTime = startDate.plusDays(day).plusMinutes(random.nextInt(MINUTES_IN_DAY)).truncatedTo(ChronoUnit.SECONDS); // Обрезаем микросекунды
                    cdrRecord.setStartTime(startTime);
                    cdrRecord.setEndTime(startTime.plusMinutes(random.nextInt(MAX_CALL_DURATION_MINUTES)).truncatedTo(ChronoUnit.SECONDS)); // Обрезаем микросекунды

                    cdrRepository.save(cdrRecord);
                }
            }
        }
    }

    public UUID generateCdrReport(String msisdn, LocalDateTime startDate, LocalDateTime endDate) {
        List<CdrRecord> cdrRecords = cdrRepository.findByFromMsisdnOrToMsisdnAndStartTimeBetween(msisdn, msisdn, startDate, endDate);
        UUID reportId = UUID.randomUUID();
        String fileName = "reports/" + msisdn + "_" + reportId + ".csv";

        // Создаем директорию, если она не существует
        File directory = new File("reports");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Форматтер для времени без микросекунд
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try (FileWriter writer = new FileWriter(fileName)) {
            for (CdrRecord record : cdrRecords) {
                writer.write(String.format("%s,%s,%s,%s,%s\n",
                        record.getCallType(),
                        record.getFromMsisdn(),
                        record.getToMsisdn(),
                        record.getStartTime().format(formatter), // Форматируем время начала
                        record.getEndTime().format(formatter))); // Форматируем время окончания
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании CDR отчета", e);
        }

        return reportId;
    }
}