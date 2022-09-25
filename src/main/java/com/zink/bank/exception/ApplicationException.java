package com.zink.bank.exception;

import lombok.Data;

@Data
public class ApplicationException extends RuntimeException {

	private final String message;

	public ApplicationException(String message) {
		this.message = message;
	}

}