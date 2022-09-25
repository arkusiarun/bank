package com.zink.bank.enums;

public enum DenominationsEnum {
    FIFTY(50), TWENTY(20), TEN(10), FIVE(5);

    private final Integer value;

    public Integer getValue() {
        return value;
    }

    DenominationsEnum(Integer value) {
        this.value = value;
    }

}