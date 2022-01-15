package com.nice.todolist.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TodoNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 3430677782573260012L;
	
	private List<String> errors = new ArrayList<String>();
	
	public TodoNotFoundException(String message) {
        super(message);
        this.errors.add(message);
    }
}
