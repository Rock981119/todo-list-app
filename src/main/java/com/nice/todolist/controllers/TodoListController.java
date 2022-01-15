package com.nice.todolist.controllers;

import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nice.todolist.dto.TodoListDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.entities.TodoList;
import com.nice.todolist.services.TodoListService;

@RestController
@RequestMapping(value="/api/todoLists",
                produces=MediaType.APPLICATION_JSON_VALUE)
public class TodoListController extends BaseController {

	private TodoListService todoListService;
	
	@Autowired
	public TodoListController(TodoListService todoListService){
		this.todoListService = todoListService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TodoListDto createTodoList(@RequestBody @Valid TodoListDto todoListDto){
		TodoList addedTodoList = todoListService.createTodoList(todoListDto);
		TodoListDto resTodoListDto = new TodoListDto();
		BeanUtils.copyProperties(addedTodoList, resTodoListDto);
		return resTodoListDto;
	}
	
	@GetMapping(value = "/{todoListId}")
    public TodoListDto getAllTasksForGivenTodoList(@PathVariable("todoListId") Long todoListId) {
		TodoList foundTodoList = todoListService.listAllTasks(todoListId);
		TodoListDto resTodoListDto = new TodoListDto();
		BeanUtils.copyProperties(foundTodoList, resTodoListDto);
        return resTodoListDto;
    }
	
	@DeleteMapping(value="/{todoListId}")
    public void deleteTask(@PathVariable("todoListId") Long todoListId) {        
		todoListService.deleteTodoList(todoListId);
    }
	
	@GetMapping(value = "/tasksGroupByTodoList")
    public Map<Long,Set<Task>> tasksGroupByTodoList() {
		Map<Long,Set<Task>> tasksGroupByTodoListId = todoListService.groupTasksByTodoList();
        return tasksGroupByTodoListId;
    }
}
