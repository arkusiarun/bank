package com.zink.bank.enums;

public enum TransactionStatus {
    INIT("INIT"), HOLD("HOLD"), COMPLETED("COMPLETED"), FAILED("FAILED");

    private String label;

    public String getLabel() {
        return label;
    }

    TransactionStatus(String label) {
        this.label = label;
    }
}