package com.nice.todolist.services;

import java.util.Map;
import java.util.Set;

import com.nice.todolist.dto.TodoListDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.entities.TodoList;

public interface TodoListService {
	
	public TodoList createTodoList(TodoListDto todoListDto);
	public TodoList listAllTasks(Long todoListId);
	public void deleteTodoList(Long todoListId);
	public Map<Long,Set<Task>> groupTasksByTodoList();
}
