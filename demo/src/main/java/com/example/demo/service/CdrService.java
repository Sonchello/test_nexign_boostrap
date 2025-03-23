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

/**
 * Сервис для работы с CDR-записями, реализует функциональность генерации случайных CDR-записей и формирования отчетов
 */
@Service
public class CdrService {

    @Autowired
    private CdrRepository cdrRepository;

    @Autowired
    private AbonentRepository abonentRepository;

    // Константы для генерации записей
    private static final int DAYS_IN_YEAR = 365;
    private static final int MINUTES_IN_DAY = 1440;
    private static final int MAX_CALL_DURATION_MINUTES = 60;
    private static final int MAX_CALLS_PER_DAY = 10;

    /**
     * Генерация случайных CDR-записей для всех абонентов за последний год
     * Для каждого дня и каждого абонента генерируется случайное количество вызовов
     */
    public void generateCdrRecords() {
        List<Abonent> abonents = abonentRepository.findAll();
        Random random = new Random();
        LocalDateTime startDate = LocalDateTime.now().minusYears(1).truncatedTo(ChronoUnit.SECONDS);

        // Генерация CDR записей за каждый день в течение года
        for (int day = 0; day < DAYS_IN_YEAR; day++) {
            for (Abonent caller : abonents) {
                int callCount = random.nextInt(MAX_CALLS_PER_DAY);

                // Генерация случайных вызовов для абонента
                for (int call = 0; call < callCount; call++) {
                    Abonent receiver;
                    do {
                        receiver = abonents.get(random.nextInt(abonents.size()));
                    } while (receiver.getMsisdn().equals(caller.getMsisdn())); // Проверка на идентичные номера

                    CdrRecord cdrRecord = new CdrRecord();
                    cdrRecord.setCallType(random.nextBoolean() ? "01" : "02");
                    cdrRecord.setFromMsisdn(caller.getMsisdn());
                    cdrRecord.setToMsisdn(receiver.getMsisdn());

                    LocalDateTime startTime = startDate.plusDays(day).plusMinutes(random.nextInt(MINUTES_IN_DAY)).truncatedTo(ChronoUnit.SECONDS);
                    cdrRecord.setStartTime(startTime);
                    cdrRecord.setEndTime(startTime.plusMinutes(random.nextInt(MAX_CALL_DURATION_MINUTES)).truncatedTo(ChronoUnit.SECONDS));

                    cdrRepository.save(cdrRecord); // Сохраняем запись
                }
            }
        }
    }

    /**
     * Генерация отчета по CDR-записям для указанного абонента за заданный период времени
     * Отчет сохраняется в файл в формате CSV в папке "reports"
     */
    public UUID generateCdrReport(String msisdn, LocalDateTime startDate, LocalDateTime endDate) {
        List<CdrRecord> cdrRecords = cdrRepository.findCallsByMsisdnAndPeriod(msisdn, startDate, endDate);
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
            // Записываем CDR записи в файл
            for (CdrRecord record : cdrRecords) {
                writer.write(String.format("%s,%s,%s,%s,%s\n",
                        record.getCallType(),
                        record.getFromMsisdn(),
                        record.getToMsisdn(),
                        record.getStartTime().format(formatter),
                        record.getEndTime().format(formatter)));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании CDR отчета", e);
        }

        return reportId;
    }
}
