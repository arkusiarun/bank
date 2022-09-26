package com.zink.bank.service.impl;

import com.zink.bank.dto.Denominations;
import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.entity.Account;
import com.zink.bank.enums.DenominationsEnum;
import com.zink.bank.enums.TransactionType;
import com.zink.bank.exception.ApplicationException;
import com.zink.bank.factory.TransactionFactory;
import com.zink.bank.mappers.AccountMapper;
import com.zink.bank.mappers.DenominationsMapper;
import com.zink.bank.mappers.TransactionMapper;
import com.zink.bank.service.TransactionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AtmServiceImplTest {

    List<com.zink.bank.entity.Denominations> denominationsList;
    private BalanceCheckService balanceCheckService;
    private CashDepositService cashDepositService;
    private CashWithdrawalService cashWithdrawalService;

    private AtmServiceImpl atmService;

    private Account account;

    @Mock
    private TransactionRequest transactionRequest;

    @Mock
    private Denominations denominationsDto;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private DenominationsMapper denominationsMapper;

    @Before
    public void init() {
        Mockito.when(denominationsDto.getFifties()).thenReturn(3);
        Mockito.when(denominationsDto.getTwenties()).thenReturn(2);
        Mockito.when(denominationsDto.getTens()).thenReturn(1);
        Mockito.when(denominationsDto.getFives()).thenReturn(1);
        atmService = new AtmServiceImpl(denominationsMapper);
        balanceCheckService = new BalanceCheckService(accountMapper);
        cashDepositService = new CashDepositService(accountMapper, transactionMapper, denominationsMapper);
        cashWithdrawalService = new CashWithdrawalService(accountMapper, denominationsMapper, transactionMapper);
        account = Mockito.mock(Account.class);
        Mockito.when(this.accountMapper.selectByAccountNoAccount(1001)).thenReturn(account);
        initialiseFactory();
        initialiseDenominations();
    }

    private void initialiseFactory() {
        List<TransactionService> atmServices = new ArrayList<>();
        atmServices.add(balanceCheckService);
        atmServices.add(cashDepositService);
        atmServices.add(cashWithdrawalService);
        new TransactionFactory(atmServices);
    }

    private void initialiseDenominations() {
        denominationsList = new ArrayList<>();
        com.zink.bank.entity.Denominations denominationFifty = createDenomination(1, DenominationsEnum.FIFTY.name(), DenominationsEnum.FIFTY.getValue(), 100, 4, 10);
        com.zink.bank.entity.Denominations denominationTwenty = createDenomination(2, DenominationsEnum.TWENTY.name(), DenominationsEnum.TWENTY.getValue(), 200, 6, 12);
        com.zink.bank.entity.Denominations denominationTen = createDenomination(3, DenominationsEnum.TEN.name(), DenominationsEnum.TEN.getValue(), 250, 7, 13);
        com.zink.bank.entity.Denominations denominationFive = createDenomination(4, DenominationsEnum.FIVE.name(), DenominationsEnum.FIVE.getValue(), 150, 10, 14);
        denominationsList.add(denominationFifty);
        denominationsList.add(denominationTwenty);
        denominationsList.add(denominationTen);
        denominationsList.add(denominationFive);
        Mockito.when(this.denominationsMapper.findAllDenominations()).thenReturn(denominationsList);
    }

    private com.zink.bank.entity.Denominations createDenomination(Integer id, String name, Integer value, Integer depositLimit, Integer disbursementLimit, Integer count) {
        com.zink.bank.entity.Denominations denominations = new com.zink.bank.entity.Denominations();
        denominations.setId(id);
        denominations.setName(name);
        denominations.setValue(value);
        denominations.setActive(true);
        denominations.setDepositLimit(depositLimit);
        denominations.setDisburseLimit(disbursementLimit);
        denominations.setCount(count);
        return denominations;
    }

    @Test(expected = ApplicationException.class)
    public void doTransaction_invalidBalanceCheck() {
        Mockito.when(transactionRequest.getTransactionType()).thenReturn(TransactionType.BALANCE_CHECK);
        Mockito.when(transactionRequest.getTransactionAmount()).thenReturn(100.0);
        this.atmService.doTransaction(transactionRequest);
    }

    @Test(expected = ApplicationException.class)
    public void doTransaction_invalidCashWithdrawal() {
        Mockito.when(transactionRequest.getTransactionType()).thenReturn(TransactionType.CASH_WITHDRAWAL);
        Mockito.when(transactionRequest.getTransactionAmount()).thenReturn(0.0);
        this.atmService.doTransaction(transactionRequest);
    }

    @Test(expected = ApplicationException.class)
    public void doTransaction_invalidCashDeposit() {
        Mockito.when(transactionRequest.getTransactionType()).thenReturn(TransactionType.CASH_DEPOSIT);
        Mockito.when(transactionRequest.getTransactionAmount()).thenReturn(0.0);
        this.atmService.doTransaction(transactionRequest);
    }

    @Test
    public void addDenominations() {
        this.atmService.addDenominations(denominationsDto);
    }

    @Test
    public void doTransaction_balanceEnquiry() {
        Mockito.when(transactionRequest.getTransactionType()).thenReturn(TransactionType.BALANCE_CHECK);
        Mockito.when(transactionRequest.getAccountNo()).thenReturn(1001);
        Assert.assertEquals(String.valueOf(1001), String.valueOf(this.atmService.doTransaction(transactionRequest).getAccountNo()));
    }

    @Test
    public void doTransaction_cashDeposit() {
        Mockito.when(transactionRequest.getTransactionType()).thenReturn(TransactionType.CASH_DEPOSIT);
        Mockito.when(transactionRequest.getAccountNo()).thenReturn(1001);
        Mockito.when(transactionRequest.getTransactionAmount()).thenReturn(205.0);
        Mockito.when(transactionRequest.getDenominations()).thenReturn(denominationsDto);
        this.atmService.doTransaction(transactionRequest);
    }

    @Test
    public void doTransaction_cashWithdrawal() {
        Mockito.when(transactionRequest.getTransactionType()).thenReturn(TransactionType.CASH_WITHDRAWAL);
        Mockito.when(transactionRequest.getAccountNo()).thenReturn(1001);
        Mockito.when(transactionRequest.getTransactionAmount()).thenReturn(205.0);
        Mockito.when(account.getClearBalance()).thenReturn(1005.0);
        Mockito.when(account.getUnclearBalance()).thenReturn(100.0);
        this.atmService.doTransaction(transactionRequest);
    }
}