package com.zink.bank.utils;

import com.google.gson.Gson;
import com.zink.bank.constants.ErrorConstants;
import com.zink.bank.dto.Denominations;
import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.dto.TransactionResponse;
import com.zink.bank.entity.Transaction;
import com.zink.bank.enums.DenominationsEnum;
import com.zink.bank.enums.TransactionStatus;
import com.zink.bank.enums.TransactionType;
import com.zink.bank.exception.ApplicationException;

import java.sql.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CommonUtils {

    private CommonUtils() {
    }

    private static final Gson gson = new Gson();

    public static String getJson(Object obj) {
        return gson.toJson(obj);
    }

    public static String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    public static List<com.zink.bank.entity.Denominations> denominationHandling(
            Denominations denominationRequest, List<com.zink.bank.entity.Denominations> denominationsList) {
        Integer fifties = denominationRequest.getFifties();
        Integer twenties = denominationRequest.getTwenties();
        Integer tens = denominationRequest.getTens();
        Integer fives = denominationRequest.getFives();
        for (com.zink.bank.entity.Denominations denominations : denominationsList) {
            Integer total = 0;
            Integer toAdd = denominations.getDepositLimit() - denominations.getCount();
            switch (DenominationsEnum.valueOf(denominations.getName())) {
                case FIFTY:
                    if (fifties + denominations.getCount() < toAdd) {
                        total = fifties + denominations.getCount();
                    }
                    break;
                case TWENTY:
                    if (twenties < toAdd) {
                        total = twenties + denominations.getCount();
                    }
                    break;
                case TEN:
                    if (tens < toAdd) {
                        total = tens + denominations.getCount();
                    }
                    break;
                case FIVE:
                    if (fives < toAdd) {
                        total = fives + denominations.getCount();
                    }
                    break;
            }
            denominations.setCount(denominations.getDepositLimit() > total ? total : denominations.getDepositLimit());
        }
        return denominationsList;
    }

    public static TransactionResponse getTransactionResponse(TransactionRequest transactionRequest, Double balance) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionId(generateTransactionId());
        if (!TransactionType.BALANCE_CHECK.equals(transactionRequest.getTransactionType())) {
            transactionResponse.setTransactionAmount(
                    transactionRequest.getTransactionAmount());
        }
        transactionResponse.setTransactionType(
                transactionRequest.getTransactionType().getLabel());
        transactionResponse.setAccountNo(transactionRequest.getAccountNo());
        transactionResponse.setRemainingBalance(balance);
        return transactionResponse;
    }

    public static Transaction createTransaction(TransactionRequest transactionRequest, Double balance,
                                                Double newBalance, String remarks, TransactionStatus status) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
        transaction.setTransactionType(transactionRequest.getTransactionType().getLabel());
        transaction.setAccountNo(transactionRequest.getAccountNo());
        transaction.setCurrentBalance(balance);
        transaction.setTransactionAmount(transactionRequest.getTransactionAmount());
        if (newBalance != null) {
            transaction.setClearBalance(newBalance);
        } else {
            transaction.setClearBalance(balance);
        }
        transaction.setRemarks(remarks);
        transaction.setStatus(status.getLabel());
        return transaction;
    }

    public static Integer generateUserId() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return Integer.valueOf(String.format("%04d", number));
    }

    public static Integer generateAccountNo() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return Integer.valueOf(String.format("%06d", number));
    }

    public static void validateTransaction(TransactionRequest transactionRequest) {
        switch (transactionRequest.getTransactionType()) {
            case CASH_DEPOSIT:
                if (transactionRequest.getTransactionAmount() <= 0 && transactionRequest.getDenominations() == null) {
                    throw new ApplicationException(ErrorConstants.INVALID_PARAMS + transactionRequest.getTransactionType());
                }
                break;
            case CASH_WITHDRAWAL:
                if (transactionRequest.getTransactionAmount() <= 0) {
                    throw new ApplicationException(ErrorConstants.INVALID_PARAMS + transactionRequest.getTransactionType());
                }
                break;
            case BALANCE_CHECK:
                if (transactionRequest.getTransactionAmount() != 0) {
                    throw new ApplicationException(ErrorConstants.INVALID_PARAMS + transactionRequest.getTransactionType());
                }
                break;
            default:
                throw new ApplicationException(ErrorConstants.INVALID_TRANSACTION_TYPE);
        }
    }
}