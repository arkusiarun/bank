package com.zink.bank.dto;

import com.zink.bank.entity.Transaction;
import com.zink.bank.utils.CommonUtils;
import lombok.Data;

import java.util.List;

@Data
public class TransactionSummary {

    private Integer accountNo;

    private List<Transaction> transactionList;

    public TransactionSummary(Integer accountNo, List  transactionList) {
        this.accountNo =  accountNo;
        this.transactionList = transactionList;
    }

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}