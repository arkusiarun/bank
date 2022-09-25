package com.zink.bank.mappers;

import com.zink.bank.entity.Denominations;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DenominationsMapper {

    List<Denominations> findAllDenominations();

    void updateDenominations(Denominations denominationsList);
}