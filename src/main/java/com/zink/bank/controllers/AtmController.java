package com.zink.bank.controllers;

import com.zink.bank.dto.Denominations;
import com.zink.bank.dto.Response;
import com.zink.bank.dto.TransactionRequest;
import com.zink.bank.dto.TransactionResponse;
import com.zink.bank.service.AtmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AtmController {

    @Autowired
    private AtmService atmService;

    @PostMapping(value = "/atm/transaction")
    public ResponseEntity<Response<TransactionResponse>> doTransaction(@RequestBody TransactionRequest transactionRequest) {
        log.info("Transaction Request Received: {}", transactionRequest);
        return new ResponseEntity<>(Response.successResponse(this.atmService.doTransaction(transactionRequest)), HttpStatus.OK);
    }

    @PostMapping(value = "/atm/add/denominations")
    public ResponseEntity<Response<?>> addDenominations(@RequestBody Denominations denominations) {
        log.info("Adding Money to  ATM : {}", denominations);
        this.atmService.addDenominations(denominations);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.OK);
    }
}