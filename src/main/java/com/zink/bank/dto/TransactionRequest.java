package com.zink.bank.dto;

import com.zink.bank.enums.TransactionType;
import com.zink.bank.utils.CommonUtils;
import lombok.Data;

@Data
public class TransactionRequest {

    private Integer customerId;

    private Integer accountNo;

    private double transactionAmount;

    private TransactionType transactionType;

    private Denominations denominations;

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}