package com.zink.bank.dto;

import com.zink.bank.entity.Account;
import com.zink.bank.utils.CommonUtils;
import lombok.Data;

import java.util.List;

@Data
public class AccountSummary {

    private Integer customerId;

    private List<Account> accounts;

    public AccountSummary(Integer customerId, List accounts) {
        this.customerId = customerId;
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}