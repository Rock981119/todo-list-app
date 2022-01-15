package com.nice.todolist.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TaskAssignmentRequest {
	
	@NotNull
	private Long taskId;
	
	@NotNull
	private Long userId;
}
