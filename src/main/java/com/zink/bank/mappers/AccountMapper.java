package com.zink.bank.mappers;

import com.zink.bank.entity.Account;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {

    List<Account> selectAccountsForCustomer(Integer customerId);

    Account selectByAccountNoAccount(Integer accountNo);

    Integer createAccount(Account account);

    void updateAccount(Account account);
}
