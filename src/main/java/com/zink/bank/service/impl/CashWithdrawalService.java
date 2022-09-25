package com.zink.bank.service.impl;

import com.zink.bank.constants.AppConstants;
import com.zink.bank.constants.ErrorConstants;
import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.dto.TransactionResponse;
import com.zink.bank.entity.Account;
import com.zink.bank.entity.Denominations;
import com.zink.bank.enums.DenominationsEnum;
import com.zink.bank.enums.TransactionStatus;
import com.zink.bank.enums.TransactionType;
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
public class CashWithdrawalService implements TransactionService {

    private final AccountMapper accountMapper;

    private final DenominationsMapper denominationsMapper;

    private final TransactionMapper transactionMapper;

    @Autowired
    public CashWithdrawalService(AccountMapper accountMapper,
                                 DenominationsMapper denominationsMapper,
                                 TransactionMapper transactionMapper) {
        this.accountMapper = accountMapper;
        this.denominationsMapper = denominationsMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.CASH_WITHDRAWAL;
    }

    @Override
    public TransactionResponse doTransaction(TransactionRequest transactionRequest) {
        log.info("Cash Withdrawal Factory Called for Request: {}", transactionRequest);
        com.zink.bank.dto.Denominations denominations = null;
        TransactionStatus status;
        Account account = this.accountMapper.selectByAccountNoAccount(transactionRequest.getAccountNo());
        double balance = account.getClearBalance() - account.getUnclearBalance();
        double newBalance = 0;
        String remarks;
        account.setLastTransactionDate(new Date(System.currentTimeMillis()));
        if (balance > transactionRequest.getTransactionAmount()) {
            denominations = checkForDenominations(transactionRequest.getTransactionAmount());
            if (denominations == null) {
                remarks = ErrorConstants.WITHDRAWAL_FAILURE;
                status = TransactionStatus.FAILED;
            } else {
                newBalance = balance - transactionRequest.getTransactionAmount();
                remarks = AppConstants.WITHDRAWAL_SUCCESS;
                account.setClearBalance(newBalance);
                status = TransactionStatus.COMPLETED;
            }
        } else {
            remarks = ErrorConstants.INSUFFICIENT_BALANCE;
            status = TransactionStatus.FAILED;
        }

        this.transactionMapper.createTransaction(CommonUtils.createTransaction(
                transactionRequest, balance, newBalance, remarks, status));
        this.accountMapper.updateAccount(account);
        TransactionResponse transactionResponse = CommonUtils.getTransactionResponse(transactionRequest, newBalance == 0 ? balance : newBalance);
        transactionResponse.setDenominations(denominations);
        return transactionResponse;
    }

    private com.zink.bank.dto.Denominations checkForDenominations(Double transactionAmount) {
        List<Denominations> denominationsList = this.denominationsMapper.findAllDenominations();
        Double tempAmount = transactionAmount;
        com.zink.bank.dto.Denominations disburseLimit = new com.zink.bank.dto.Denominations();
        com.zink.bank.dto.Denominations currentCount = new com.zink.bank.dto.Denominations();
        com.zink.bank.dto.Denominations disbursementDetails = new com.zink.bank.dto.Denominations();
        for (Denominations denominations : denominationsList) {
            switch (DenominationsEnum.valueOf(denominations.getName())) {
                case FIFTY:
                    disburseLimit.setFifties(denominations.getDisburseLimit());
                    currentCount.setFifties(denominations.getCount());
                    break;
                case TWENTY:
                    disburseLimit.setTwenties(denominations.getDisburseLimit());
                    currentCount.setTwenties(denominations.getCount());
                    break;
                case TEN:
                    disburseLimit.setTens(denominations.getDisburseLimit());
                    currentCount.setTens(denominations.getCount());
                    break;
                case FIVE:
                    disburseLimit.setFives(denominations.getDisburseLimit());
                    currentCount.setFives(denominations.getCount());
                    break;
            }
        }
        if (tempAmount >= DenominationsEnum.FIFTY.getValue()) {
            Integer fiftyCount = tempAmount.intValue() / DenominationsEnum.FIFTY.getValue();
            if (fiftyCount > disburseLimit.getFifties()) {
                fiftyCount = disburseLimit.getFifties() - currentCount.getFifties();
            } else {
                if (fiftyCount > currentCount.getFifties()) {
                    fiftyCount = currentCount.getFifties();
                }
            }
            currentCount.setFifties(currentCount.getFifties() - fiftyCount);
            disbursementDetails.setFifties(fiftyCount);
            tempAmount = tempAmount - (fiftyCount * DenominationsEnum.FIFTY.getValue());
        }

        if (tempAmount >= DenominationsEnum.TWENTY.getValue()) {
            Integer twentyCount = tempAmount.intValue() / DenominationsEnum.TWENTY.getValue();
            if (twentyCount > disburseLimit.getTwenties()) {
                twentyCount = disburseLimit.getTwenties() - currentCount.getTwenties();
            } else {
                if (twentyCount > currentCount.getTwenties()) {
                    twentyCount = currentCount.getTwenties();
                }
            }
            currentCount.setTwenties(currentCount.getTwenties() - twentyCount);
            disbursementDetails.setTwenties(twentyCount);
            tempAmount = tempAmount - (twentyCount * DenominationsEnum.TWENTY.getValue());
        }

        if (tempAmount >= DenominationsEnum.TEN.getValue()) {
            Integer tenCount = tempAmount.intValue() / DenominationsEnum.TEN.getValue();
            if (tenCount > disburseLimit.getTwenties()) {
                tenCount = disburseLimit.getTwenties() - currentCount.getTwenties();
            } else {
                if (tenCount > currentCount.getTwenties()) {
                    tenCount = currentCount.getTwenties();
                }
            }
            currentCount.setTens(currentCount.getTwenties() - tenCount);
            disbursementDetails.setTens(tenCount);
            tempAmount = tempAmount - (tenCount * DenominationsEnum.TEN.getValue());
        }

        if (tempAmount >= DenominationsEnum.FIVE.getValue()) {
            Integer fiveCount = tempAmount.intValue() / DenominationsEnum.FIVE.getValue();
            if (fiveCount > disburseLimit.getFives()) {
                fiveCount = disburseLimit.getFives() - currentCount.getFives();
            } else {
                if (fiveCount > currentCount.getFives()) {
                    fiveCount = currentCount.getFives();
                }
            }
            currentCount.setFives(currentCount.getFives() - fiveCount);
            disbursementDetails.setFives(fiveCount);
            tempAmount = tempAmount - (fiveCount * DenominationsEnum.FIVE.getValue());
        }
        if (tempAmount > 0) {
            return null;
        } else {
            for (Denominations denominations : denominationsList) {
                switch (DenominationsEnum.valueOf(denominations.getName())) {
                    case FIFTY:
                        denominations.setCount(currentCount.getFifties());
                        break;
                    case TWENTY:
                        denominations.setCount(currentCount.getTwenties());
                        break;
                    case TEN:
                        denominations.setCount(currentCount.getTens());
                        break;
                    case FIVE:
                        denominations.setCount(currentCount.getFives());
                        break;
                }
                this.denominationsMapper.updateDenominations(denominations);
            }
        }
        return disbursementDetails;
    }
}