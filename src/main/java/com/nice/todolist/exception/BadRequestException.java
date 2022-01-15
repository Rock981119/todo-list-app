package com.nice.todolist.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String errCode;
	private List<String> errors = new ArrayList<String>();
	
	public BadRequestException(String errMsg) {
		super(errMsg);
		this.errors.add(errMsg);
	}
}
