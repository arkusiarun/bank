package com.zink.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zink.bank.utils.CommonUtils;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateAccountResponse {

    private Integer customerId;

    private Integer accountNumber;

    public CreateAccountResponse(Integer customerId, Integer accountNumber) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}