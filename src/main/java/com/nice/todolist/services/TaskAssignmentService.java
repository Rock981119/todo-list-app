package com.nice.todolist.services;

import java.util.List;

import com.nice.todolist.model.TaskAssignmentResponse;

public interface TaskAssignmentService {
     
	public List<TaskAssignmentResponse> getAllTasksWithAssignedUsers();
	public List<TaskAssignmentResponse> getCompletedTasksWithAssignedUsers();
	public List<TaskAssignmentResponse> getNotCompletedTasksWithAssignedUsers();
	public List<TaskAssignmentResponse> getInProgressTasksWithAssignedUsers();
	public List<TaskAssignmentResponse> getNotStartedTasksWithAssignedUsers();
}
