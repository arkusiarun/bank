package com.zink.bank.enums;

public enum TransactionType {
    CASH_WITHDRAWAL("Cash Withdrawal"),
    CASH_DEPOSIT("Cash Deposit"),
    BALANCE_CHECK("Balance Check");

    private String label;

    public String getLabel() {
        return label;
    }

    TransactionType(String label) {
        this.label = label;
    }

}