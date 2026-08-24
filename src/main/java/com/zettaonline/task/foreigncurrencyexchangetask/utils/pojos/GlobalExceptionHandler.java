package com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos;

import com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions.BalanceNotFoundException;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions.ClientNotFoundException;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions.InsufficientFundsException;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ClientNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleClientNotFound(ClientNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse("CLIENT_NOT_FOUND", exception.getMessage()));
	}

	@ExceptionHandler(BalanceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleBalanceNotFound(BalanceNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse("BALANCE_NOT_FOUND", exception.getMessage()));
	}

	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException exception) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorResponse("INSUFFICIENT_FUNDS", exception.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
		return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", "Invalid conversion request"));
	}
}