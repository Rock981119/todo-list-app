/**
 * 
 */
package com.nice.todolist.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nice.todolist.dto.TodoListDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.entities.TodoList;
import com.nice.todolist.repositories.TodoListRepository;
import com.nice.todolist.services.TodoListService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author snyala
 *
 */
@Slf4j
@Service
public class TodoListServiceImpl implements TodoListService {

	@Autowired
	private TodoListRepository todoListRepository;
	
	@Override
	public TodoList createTodoList(TodoListDto todoListDto) {
		TodoList todoList = new TodoList();
		BeanUtils.copyProperties(todoListDto, todoList);
		todoList.setCreatedDate(new Date());
		if(Objects.nonNull(todoList.getTasks())){
			List<Task> tasks = todoList.getTasks() .stream()
			                    .map(this::setCreatedDate)
			                    .collect(Collectors.toList());
			todoList.setTasks(new HashSet<Task>(tasks));
		}
		return todoListRepository.save(todoList);
	}

	/**
	 * @return
	 */
	private Task setCreatedDate(Task task) {
		task.setCreatedDate(new Date());
		return task;
	}

	/* (non-Javadoc)
	 * @see com.nice.todolist.services.TodoListService#listAllTasks(java.lang.Long)
	 */
	@Override
	public TodoList listAllTasks(Long todoListId) {		
		return todoListRepository.findOne(todoListId);
	}

	/* (non-Javadoc)
	 * @see com.nice.todolist.services.TodoListService#deleteTodoList(java.lang.Long)
	 */
	@Override
	public void deleteTodoList(Long todoListId) {
		todoListRepository.delete(todoListId);
	}

	/* (non-Javadoc)
	 * @see com.nice.todolist.services.TodoListService#groupTasksByTodoList()
	 */
	@Override
	public Map<Long, Set<Task>> groupTasksByTodoList() {
		List<TodoList> allTodoLists = todoListRepository.findAll();
		Map<Long, Set<Task>> resultMap = new HashMap<>();
		for(TodoList item:allTodoLists){
			resultMap.put(item.getId(), item.getTasks());
		}
		return resultMap;
	}

}
