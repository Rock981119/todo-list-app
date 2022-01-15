package com.nice.todolist.controllers;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nice.todolist.dto.TaskDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.exception.TodoException;
import com.nice.todolist.model.TaskAssignmentRequest;
import com.nice.todolist.model.TaskAssignmentResponse;
import com.nice.todolist.services.TaskAssignmentService;
import com.nice.todolist.services.TaskService;

@RestController
@RequestMapping(value="/api/tasks", produces=MediaType.APPLICATION_JSON_VALUE)
public class TaskController extends BaseController {

	private TaskService taskService;
	
	@Autowired
	private TaskAssignmentService taskAssignmentService;
	
	@Autowired
	public TaskController(TaskService taskService){
		this.taskService = taskService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TaskDto createNewTask(@RequestBody @Valid TaskDto taskDto){
		Task addedTask = taskService.createTask(taskDto);
		if(Objects.nonNull(addedTask)){
		   BeanUtils.copyProperties(addedTask, taskDto);
		}
		return taskDto;
	}

	@GetMapping
	public List<Task> getAllTasks(){
		return taskService.getAllTasks();
	}

	
	@PatchMapping(value="/{id}")
    public Task updateTask(@PathVariable("id") Long id, @RequestBody @Valid TaskDto updatedTaskDto) {
		updatedTaskDto.setId(id);
        return taskService.updateTask(updatedTaskDto);
    }
	
	@DeleteMapping(value="/{id}")
    public TaskDto deleteTask(@PathVariable("id") Long id) {        
		Task deleted = taskService.deleteById(id);
		return convertFromUserEntityToDto(deleted);
    }
	
	@PostMapping(value="/assign")
	public TaskAssignmentResponse assignTask(@RequestBody @Valid TaskAssignmentRequest taskAssignmentRequest) {
		return taskService.assignTask(taskAssignmentRequest);
	}
	
	@GetMapping(value="/withAssignedUsers")
	public List<TaskAssignmentResponse> getAllTasksWithAssignedUsers() {
		return taskAssignmentService.getAllTasksWithAssignedUsers();
	}
	
	private TaskDto convertFromUserEntityToDto(Task task) {
		TaskDto taskDto = new TaskDto();
		
		taskDto.setId(task.getId());
		taskDto.setName(task.getName());
		taskDto.setDescription(task.getDescription());
		taskDto.setStatus(task.getStatus());
		taskDto.setCreatedDate(task.getCreatedDate());
		
		return taskDto;
	}
}
