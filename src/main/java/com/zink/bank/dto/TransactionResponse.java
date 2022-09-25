package com.zink.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zink.bank.utils.CommonUtils;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    private String transactionId;

    private Integer accountNo;

    private String transactionType;

    private double transactionAmount;

    private double remainingBalance;

    private Denominations denominations;

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}