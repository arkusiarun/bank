package com.zink.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zink.bank.utils.CommonUtils;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserResponse {

    private Integer userId;

    public CreateUserResponse(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}