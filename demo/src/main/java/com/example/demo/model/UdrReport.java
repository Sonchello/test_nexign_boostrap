package com.example.demo.model;

import lombok.Data;

@Data
public class UdrReport {
    private String msisdn;
    private CallDetail incomingCall;
    private CallDetail outcomingCall;

    @Data
    public static class CallDetail {
        private String totalTime;
    }
}