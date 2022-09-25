package com.zink.bank.mappers;

import com.zink.bank.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionMapper {

    List<Transaction> fetchTransaction(Integer accountNo);

    void createTransaction(Transaction transaction);
}