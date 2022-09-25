package com.zink.bank.service;


import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.dto.TransactionResponse;
import com.zink.bank.enums.TransactionType;

public interface TransactionService {

    TransactionType getTransactionType();

    TransactionResponse doTransaction(TransactionRequest transactionRequest);
}