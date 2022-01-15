/**
 * 
 */
package com.nice.todolist.util;

import java.util.Date;

import com.nice.todolist.dto.TaskDto;


/**
 * @author nshek
 *
 */
public class TaskDtoBuilder {
	private TaskDto model;
	
	public TaskDtoBuilder(){
		model = new TaskDto();
	}
	
	public TaskDtoBuilder id(Long id) {
		model.setId(id);
	    return this;
	}

	public TaskDtoBuilder name(String taskName) {
	     model.setName(taskName);
	     return this;
	}
	
	public TaskDtoBuilder description(String description) {
	     model.setDescription(description);
	     return this;
	}

	public TaskDtoBuilder status(String status) {
	     model.setStatus(status);
	     return this;
	}
	
	public TaskDtoBuilder createdDate(Date date) {
	     model.setCreatedDate(date);
	     return this;
	}
	
	public TaskDto build() {
	       return model;
	}
}
