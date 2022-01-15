package com.nice.todolist.dto;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nice.todolist.entities.Task;
import com.nice.todolist.util.JsonDateSerializer;

import lombok.Data;

@Data
public class TodoListDto {

	private Long id;
	private String name;
	private String purpose;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date createdDate;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date modifiedDate;
	
	private Set<Task> tasks;
	
}
