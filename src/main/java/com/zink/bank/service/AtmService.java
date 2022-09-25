package com.zink.bank.service;

import com.zink.bank.dto.Denominations;
import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.dto.TransactionResponse;

public interface AtmService {

    TransactionResponse doTransaction(TransactionRequest transactionRequest);

    void addDenominations(Denominations denominations);

}