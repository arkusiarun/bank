package com.zink.bank.dto;

import com.zink.bank.utils.CommonUtils;
import lombok.Data;

@Data
public class Denominations {

    private Integer fifties;

    private Integer twenties;

    private Integer tens;

    private Integer fives;

    @Override
    public String toString() {
        return CommonUtils.getJson(this);
    }
}