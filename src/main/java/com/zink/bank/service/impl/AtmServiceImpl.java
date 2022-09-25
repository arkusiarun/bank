package com.zink.bank.service.impl;

import com.zink.bank.dto.Denominations;
import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.dto.TransactionResponse;
import com.zink.bank.factory.TransactionFactory;
import com.zink.bank.mappers.DenominationsMapper;
import com.zink.bank.service.AtmService;
import com.zink.bank.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AtmServiceImpl implements AtmService {

    private DenominationsMapper denominationsMapper;

    @Autowired
    public AtmServiceImpl(DenominationsMapper denominationsMapper) {
        this.denominationsMapper = denominationsMapper;
    }

    @Override
    public TransactionResponse doTransaction(TransactionRequest transactionRequest) {
        return TransactionFactory.getParser(transactionRequest.getTransactionType())
                .doTransaction(transactionRequest);
    }

    @Override
    public void addDenominations(Denominations denominations) {
        Map<Integer, Integer> map = new HashMap<>();
        List<com.zink.bank.entity.Denominations> denominationsList = CommonUtils.denominationHandling(
                denominations, this.denominationsMapper.findAllDenominations());
        for(com.zink.bank.entity.Denominations denominations1: denominationsList) {
            this.denominationsMapper.updateDenominations(denominations1);
        }
    }
}