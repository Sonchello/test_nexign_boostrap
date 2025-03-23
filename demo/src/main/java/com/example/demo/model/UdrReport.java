package com.example.demo.model;

import lombok.Data;

/**
 * Класс, представляющий UDR-отчет, содержит информацию о входящих и исходящих звонках абонента
 */
@Data
public class UdrReport {

    /**
     * MSISDN абонента, для которого сформирован отчет
     */
    private String msisdn;

    /**
     * суммарное время входящих разговоров
     */
    private CallDetail incomingCall;

    /**
     * суммарное время исходящих разговоров
     */
    private CallDetail outcomingCall;

    /**
     * Вложенный класс, содержащий информацию о длительности звонков
     */
    @Data
    public static class CallDetail {

        /**
         * Общее время разговоров в формате строки
         */
        private String totalTime;
    }
}
