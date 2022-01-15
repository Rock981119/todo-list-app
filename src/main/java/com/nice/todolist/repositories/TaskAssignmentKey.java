package com.nice.todolist.repositories;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class TaskAssignmentKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="TASK_ID")
	private Long taskId;
	
	@Column(name="USER_ID")
	private Long userId;
}
