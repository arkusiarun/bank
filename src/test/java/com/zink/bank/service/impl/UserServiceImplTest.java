package com.zink.bank.service.impl;

import com.zink.bank.dto.CreateAccountRequest;
import com.zink.bank.dto.CreateUserRequest;
import com.zink.bank.exception.ApplicationException;
import com.zink.bank.mappers.AccountMapper;
import com.zink.bank.mappers.TransactionMapper;
import com.zink.bank.mappers.UserMapper;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private TransactionMapper transactionMapper;

    @Before
    public void init() {
        userService = new UserServiceImpl(userMapper, accountMapper, transactionMapper);

    }

    @Test
    public void createUser() {
        CreateUserRequest createUserRequest = Mockito.mock(CreateUserRequest.class);
        Mockito.when(createUserRequest.getUserName()).thenReturn("test");
        Mockito.when(createUserRequest.getPassword()).thenReturn("test");
        Assert.isNonEmpty(this.userService.createUser(createUserRequest));
    }

    @Test
    public void createAccount() {
        CreateAccountRequest createAccountRequest = Mockito.mock(CreateAccountRequest.class);
        Mockito.when(createAccountRequest.getCustomerId()).thenReturn(101);
        Mockito.when(createAccountRequest.getDepositAmount()).thenReturn(1001.0);
        Mockito.when(createAccountRequest.getCustomerName()).thenReturn("Test User");
        Mockito.when(this.userMapper.selectUser(createAccountRequest.getCustomerId())).thenReturn(101);
        Assert.isNonEmpty(this.userService.createAccount(createAccountRequest));
    }

    @Test(expected = ApplicationException.class)
    public void createAccount_userNotFound() {
        CreateAccountRequest createAccountRequest = Mockito.mock(CreateAccountRequest.class);
        this.userService.createAccount(createAccountRequest);
    }

    @Test
    public void getAccountSummary() {
        Assert.isNonEmpty(this.userService.getAccountSummary(101));
    }

    @Test
    public void getTransactionSummary() {
        Assert.isNonEmpty(this.userService.getTransactionSummary(1001));
    }
}