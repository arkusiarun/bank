package com.zink.bank.service;

import com.zink.bank.dto.*;

public interface UserService {

    CreateUserResponse createUser(CreateUserRequest createUserRequest);

    CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);

    AccountSummary getAccountSummary(Integer accountNo);

    TransactionSummary getTransactionSummary(Integer accountNo);

}