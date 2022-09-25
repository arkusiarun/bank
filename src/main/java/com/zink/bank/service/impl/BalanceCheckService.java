package com.zink.bank.service.impl;

import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.dto.TransactionResponse;
import com.zink.bank.entity.Account;
import com.zink.bank.enums.TransactionType;
import com.zink.bank.mappers.AccountMapper;
import com.zink.bank.service.TransactionService;
import com.zink.bank.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceCheckService implements TransactionService {

    private AccountMapper accountMapper;

    @Autowired
    public BalanceCheckService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.BALANCE_CHECK;
    }

    @Override
    public TransactionResponse doTransaction(TransactionRequest transactionRequest) {
        log.info("Balance Check Factory Called for Request: {}", transactionRequest);
        Account account = this.accountMapper.selectByAccountNoAccount(
                transactionRequest.getAccountNo());
        Double balance = account.getClearBalance() - account.getUnclearBalance();
        return CommonUtils.getTransactionResponse(transactionRequest, balance);
    }
}