/**
 * 
 */
package com.nice.todolist.util;

import java.util.Date;

import com.nice.todolist.entities.Task;

/**
 * @author nshek
 *
 */
public class TaskBuilder {
	private Task model;
	
	public TaskBuilder(){
		model = new Task();
	}
	
	public TaskBuilder id(Long id) {
		model.setId(id);
	    return this;
	}

	public TaskBuilder name(String taskName) {
	     model.setName(taskName);
	     return this;
	}
	
	public TaskBuilder description(String taskDesc) {
	     model.setDescription(taskDesc);
	     return this;
	}

	public TaskBuilder status(String status) {
	     model.setStatus(status);
	     return this;
	}
	
	public TaskBuilder createdDate(Date date) {
	     model.setCreatedDate(date);
	     return this;
	}
	
	public Task build() {
	       return model;
	}
}
