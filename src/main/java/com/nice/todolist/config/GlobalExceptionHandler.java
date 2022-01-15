package com.nice.todolist.config;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.nice.todolist.exception.BadRequestException;
import com.nice.todolist.exception.ErrorResponse;
import com.nice.todolist.exception.TodoException;
import com.nice.todolist.exception.TodoNotFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private MessageSource messageSource;
	
	@Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
	
	@ExceptionHandler(TodoException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleCustomException(TodoException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setSuccess(false);
		response.setErrors(ex.getErrors());
		response.setMessage("Server Error: "+ex.getMessage());
		log.error("Server Error:{}",ex);
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleTodoNotFoundException(TodoNotFoundException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setSuccess(true);
		response.setErrors(ex.getErrors());
		response.setMessage("No data found for the given input parameters");
        log.error("handling 404 error on a todo operation");
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value={HttpMessageNotReadableException.class,JsonParseException.class,IllegalArgumentException.class,TypeMismatchException.class})
	@ResponseBody
	public ResponseEntity<ErrorResponse> missingParameterExceptionHandler(Exception ex) {
		ErrorResponse response = new ErrorResponse();
		response.setSuccess(false);
		response.setErrors(Arrays.asList(ex.getMessage()));
		response.setMessage("Input request is not valid: Please change the request and retry.");
		log.error("Input request is not valid : Communication with Server failed:{}",ex);
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value={BadRequestException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> processValidationError(BadRequestException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setSuccess(false);
		response.setErrors(ex.getErrors());
		response.setMessage("Input request is not valid: Please change the request and retry.");
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value={MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorResponse response = processFieldErrors(fieldErrors);
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(SQLException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleDBException(SQLException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setSuccess(false);
		response.setErrors(Arrays.asList(ex.getMessage()));
		response.setMessage("Database Error: Communication with database failed");
		log.error("Database Error: Communication with database failed:{}",ex);
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DataAccessException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleDBException(DataAccessException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setSuccess(false);
		response.setErrors(Arrays.asList(ex.getMessage()));
		response.setMessage("Database Error: Communication with database failed.");
		log.error("Database Error: Communication with database failed:{}",ex);
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex) {
		ErrorResponse response = new ErrorResponse();
		response.setSuccess(false);
		response.setErrors(Arrays.asList(ex.getMessage()));
		response.setMessage("Unknown Error: Unable to perform the requested operation, please try again later.");
		log.error("Unknown Error: Unable to perform the requested operation, please try again later.:{}",ex);
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ErrorResponse processFieldErrors(List<FieldError> fieldErrors) {
		ErrorResponse response = new ErrorResponse();
		response.setSuccess(false);
        
        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            response.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return response;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale =  LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
        return localizedErrorMessage;
    }

}
