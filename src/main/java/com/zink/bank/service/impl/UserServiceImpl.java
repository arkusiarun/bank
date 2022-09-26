package com.zink.bank.service.impl;

import com.zink.bank.constants.ErrorConstants;
import com.zink.bank.dto.*;
import com.zink.bank.entity.Account;
import com.zink.bank.entity.User;
import com.zink.bank.enums.AccountStatus;
import com.zink.bank.exception.ApplicationException;
import com.zink.bank.mappers.AccountMapper;
import com.zink.bank.mappers.TransactionMapper;
import com.zink.bank.mappers.UserMapper;
import com.zink.bank.service.UserService;
import com.zink.bank.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    private AccountMapper accountMapper;

    private TransactionMapper transactionMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           AccountMapper accountMapper,
                           TransactionMapper transactionMapper) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        log.info("Creating User for Request: {}", createUserRequest);
        User user = new User();
        Integer userId = CommonUtils.generateUserId();
        user.setId(userId);
        user.setUserName(createUserRequest.getUserName());
        user.setPassword(createUserRequest.getPassword());
        user.setLastLoggedInDate(new Date(System.currentTimeMillis()));
        this.userMapper.createUser(user);
        return new CreateUserResponse(userId);
    }

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        log.info("Creating Account for Request: {}", createAccountRequest);
        Integer customerId = this.userMapper.selectUser(createAccountRequest.getCustomerId());
        if(customerId == 0) {
            throw new ApplicationException(ErrorConstants.CUSTOMER_NOT_FOUND);
        }
        Account account = new Account();
        Integer accountNo = CommonUtils.generateAccountNo();
        account.setAccountNo(accountNo);
        account.setStatus(AccountStatus.ACTIVE.getLabel());
        account.setClearBalance(createAccountRequest.getDepositAmount());
        account.setCustomerId(createAccountRequest.getCustomerId());
        account.setCustomerName(createAccountRequest.getCustomerName());
        account.setLastTransactionDate(new Date(System.currentTimeMillis()));
        this.accountMapper.createAccount(account);
        return new CreateAccountResponse(createAccountRequest.getCustomerId(), accountNo);
    }

    @Override
    public AccountSummary getAccountSummary(Integer customerId) {
        log.info("Fetching Account Summary For Customer: {}", customerId);
        return new AccountSummary(customerId,
                this.accountMapper.selectAccountsForCustomer(customerId));
    }

    @Override
    public TransactionSummary getTransactionSummary(Integer accountNo) {
        log.info("Fetching Transaction Summary For Account: {}", accountNo);
        return new TransactionSummary(accountNo,
                this.transactionMapper.fetchTransaction(accountNo));
    }
}