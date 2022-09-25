package com.zink.bank.dto;

import com.zink.bank.utils.CommonUtils;
import lombok.Data;

@Data
public class CreateAccountRequest {

    private Integer customerId;

    private String customerName;

    private double depositAmount;

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}