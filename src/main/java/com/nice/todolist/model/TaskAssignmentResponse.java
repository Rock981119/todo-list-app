package com.nice.todolist.model;

import lombok.Data;

@Data
public class TaskAssignmentResponse {

	private Long taskId;
	private String taskName;
	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
}
