package com.zink.bank.factory;

import com.zink.bank.constants.ErrorConstants;
import com.zink.bank.enums.TransactionType;
import com.zink.bank.exception.ApplicationException;
import com.zink.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TransactionFactory {

    private static Map<TransactionType, TransactionService> transactionServiceMap = new EnumMap<>(TransactionType.class);

    @Autowired
    public TransactionFactory(List<TransactionService> transactionServices) {
        populateFactoryMap(transactionServices.stream().collect(Collectors.toMap(TransactionService:: getTransactionType, Function.identity())));
    }

    public static TransactionService getParser(TransactionType parserType) {
        return Optional.ofNullable(transactionServiceMap.get(parserType))
                .orElseThrow(() -> new ApplicationException(ErrorConstants.INVALID_TRANSACTION_TYPE));
    }

    private static void populateFactoryMap(Map<TransactionType, TransactionService> factoryMap) {
        transactionServiceMap = factoryMap;
    }
}