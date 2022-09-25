package com.zink.bank.dto;

import com.zink.bank.utils.CommonUtils;
import lombok.Data;

@Data
public class CreateUserRequest {

    private String userName;

    private String password;

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}