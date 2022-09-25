package com.zink.bank.service.impl;

import com.zink.bank.constants.AppConstants;
import com.zink.bank.constants.ErrorConstants;
import com.zink.bank.dto.Denominations;
import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.dto.TransactionResponse;
import com.zink.bank.entity.Account;
import com.zink.bank.enums.DenominationsEnum;
import com.zink.bank.enums.TransactionStatus;
import com.zink.bank.enums.TransactionType;
import com.zink.bank.exception.ApplicationException;
import com.zink.bank.mappers.AccountMapper;
import com.zink.bank.mappers.DenominationsMapper;
import com.zink.bank.mappers.TransactionMapper;
import com.zink.bank.service.TransactionService;
import com.zink.bank.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Slf4j
@Service
public class CashDepositService implements TransactionService {

    private AccountMapper accountMapper;

    private TransactionMapper transactionMapper;

    private DenominationsMapper denominationsMapper;

    @Autowired
    public CashDepositService(AccountMapper accountMapper,
                              TransactionMapper transactionMapper,
                              DenominationsMapper denominationsMapper) {
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
        this.denominationsMapper = denominationsMapper;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.CASH_DEPOSIT;
    }

    @Override
    public TransactionResponse doTransaction(TransactionRequest transactionRequest) {
        log.info("Cash Deposit Factory Called for Request: {}", transactionRequest);
        Account account = this.accountMapper.selectByAccountNoAccount(transactionRequest.getAccountNo());
        List<com.zink.bank.entity.Denominations> denominationsList = this.denominationsMapper.findAllDenominations();
        TransactionStatus status;
        Double balance = account.getClearBalance();
        double newBalance = 0;
        String remarks = null;
        boolean isTransactionAllowed = checkIfDepositAllowed(denominationsList,
                transactionRequest.getDenominations(), transactionRequest.getTransactionAmount());
        if (isTransactionAllowed) {
            newBalance = balance + transactionRequest.getTransactionAmount();
            remarks = AppConstants.DEPOSIT_SUCCESS;
            status = TransactionStatus.COMPLETED;
            account.setClearBalance(newBalance);
        } else {
            remarks = ErrorConstants.DEPOSIT_FAILURE;
            status = TransactionStatus.FAILED;
        }
        account.setLastTransactionDate(new Date(System.currentTimeMillis()));
        this.accountMapper.updateAccount(account);
        this.transactionMapper.createTransaction(CommonUtils.createTransaction(
                transactionRequest, balance, newBalance, remarks, status));
        if(!isTransactionAllowed) {
            throw new ApplicationException(ErrorConstants.DEPOSIT_FAILURE);
        }
        return CommonUtils.getTransactionResponse(transactionRequest, newBalance == 0 ? balance : newBalance);
    }

    private boolean checkIfDepositAllowed(List<com.zink.bank.entity.Denominations> denominationsList,
                                          Denominations denominationRequest, double transactionAmount) {
        boolean result = true;
        Denominations countDenominations = new Denominations();
        Integer fifties = denominationRequest.getFifties();
        Integer twenties = denominationRequest.getTwenties();
        Integer tens = denominationRequest.getTens();
        Integer fives = denominationRequest.getFives();
        Integer totalAmount = 0;
        for (com.zink.bank.entity.Denominations denominations : denominationsList) {
            Integer currentCount = 0;
            switch (DenominationsEnum.valueOf(denominations.getName())) {
                case FIFTY:
                    totalAmount = totalAmount + denominations.getValue() * fifties;
                    currentCount = denominations.getCount() + fifties;
                    countDenominations.setFifties(currentCount);
                    if (currentCount > denominations.getDepositLimit()) {
                        result = false;
                    }
                    break;
                case TWENTY:
                    totalAmount = totalAmount + denominations.getValue() * twenties;
                    currentCount = denominations.getCount() + twenties;
                    countDenominations.setTwenties(currentCount);
                    if (currentCount > denominations.getDepositLimit()) {
                        result = false;
                    }
                    break;
                case TEN:
                    totalAmount = totalAmount + denominations.getValue() * tens;
                    currentCount = denominations.getCount() + tens;
                    countDenominations.setTens(currentCount);
                    if (currentCount > denominations.getDepositLimit()) {
                        result = false;
                    }
                    break;
                case FIVE:
                    totalAmount = totalAmount + denominations.getValue() * fives;
                    currentCount = denominations.getCount() + fives;
                    countDenominations.setFives(currentCount);
                    if (currentCount > denominations.getDepositLimit()) {
                        result = false;
                    }
                    break;
            }
        }
        if (result && (totalAmount == transactionAmount)) {
            updateDenominationCount(countDenominations, denominationsList);
        }
        return result;
    }

    private void updateDenominationCount(Denominations denomination, List<com.zink.bank.entity.Denominations> denominationsList) {
        for (com.zink.bank.entity.Denominations currentDenomination : denominationsList) {
            switch (DenominationsEnum.valueOf(currentDenomination.getName())) {
                case FIFTY:
                    currentDenomination.setCount(denomination.getFifties());
                    break;
                case TWENTY:
                    currentDenomination.setCount(denomination.getTwenties());
                    break;
                case TEN:
                    currentDenomination.setCount(denomination.getTens());
                    break;
                case FIVE:
                    currentDenomination.setCount(denomination.getFives());
                    break;
            }
            this.denominationsMapper.updateDenominations(currentDenomination);
        }
    }
}