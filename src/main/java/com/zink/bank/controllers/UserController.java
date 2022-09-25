package com.zink.bank.controllers;

import com.zink.bank.dto.*;
import com.zink.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/create/user")
    public ResponseEntity<Response<CreateUserResponse>> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("Create User Request Received: {}", createUserRequest);
        return new ResponseEntity<>(Response.successResponse(this.userService.createUser(createUserRequest)), HttpStatus.CREATED);
    }

    @PostMapping(value = "/create/account")
    public ResponseEntity<Response<CreateAccountResponse>> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        log.info("Create Account Request Received: {}", createAccountRequest);
        return new ResponseEntity<>(Response.successResponse(this.userService.createAccount(createAccountRequest)), HttpStatus.CREATED);
    }

    @GetMapping(value = "/view/account/summary")
    public ResponseEntity<Response<AccountSummary>> viewAccountSummary(@RequestParam(value = "customerId") Integer customerId) {
        log.info("Checking Account Summary for Account : {}", customerId);
        return new ResponseEntity<>(Response.successResponse(this.userService.getAccountSummary(customerId)), HttpStatus.CREATED);
    }

    @GetMapping(value = "/view/transaction/summary")
    public ResponseEntity<Response<TransactionSummary>> viewTransaction(@RequestParam(value = "accountNo") Integer accountNo) {
        log.info("Checking Transaction Summary for Account : {}", accountNo);
        return new ResponseEntity<>(Response.successResponse(this.userService.getTransactionSummary(accountNo)), HttpStatus.CREATED);
    }
}