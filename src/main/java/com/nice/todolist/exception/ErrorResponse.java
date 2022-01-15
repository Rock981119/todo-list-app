package com.nice.todolist.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private boolean success;
	private String message;
	private List<String> errors = new ArrayList<String>();
	
	public void addFieldError(String path, String message) {
        errors.add(path+":"+message);
    }
}
