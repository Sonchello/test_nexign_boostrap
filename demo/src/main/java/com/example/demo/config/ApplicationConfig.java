package com.example.demo.config;

import com.example.demo.model.Abonent;
import com.example.demo.repository.AbonentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация приложения.
 * Здесь создаются данные для абонентов при старте
 */
@Configuration
public class ApplicationConfig {

    /**
     * Добавление в базу абонентов
     *
     * @param abonentRepository репозиторий, в который будем сохранять абонентов
     */
    @Bean
    public CommandLineRunner initData(AbonentRepository abonentRepository) {
        return args -> {
            String[] msisdns = {"79110000001", "79991234567", "79140001234", "79240000112", "79981231234",
                    "79185671234", "79001237890", "79527897890", "79113456789", "79245679009"};

            // Просто пробегаемся по массиву номеров и создаем абонентов
            for (String msisdn : msisdns) {
                Abonent abonent = new Abonent();
                abonent.setMsisdn(msisdn);
                abonentRepository.save(abonent);
            }
        };
    }
}
