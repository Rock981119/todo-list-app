package com.nice.todolist.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class TodoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String errCode;
	private List<String> errors = new ArrayList<String>();
	
	public TodoException(String errMsg) {
		super(errMsg);
		this.errors.add(errMsg);
	}
	
	public TodoException(String errMsg, Throwable cause) {
		super(errMsg, cause);
		this.errors.add(errMsg);
	}
}
