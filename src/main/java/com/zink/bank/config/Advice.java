package com.zink.bank.config;

import com.zink.bank.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//@ControllerAdvice(basePackages = AppConstants.CONTROLLER_BASE_PACKAGE)
public class Advice {

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Response<?>> handleException(Exception ex) {
        return new ResponseEntity<>(Response.failureResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}