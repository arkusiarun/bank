package com.zink.bank.enums;

public enum AccountStatus {
    ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

    private String label;

    public String getLabel() {
        return label;
    }

    AccountStatus(String label) {
        this.label = label;
    }
}