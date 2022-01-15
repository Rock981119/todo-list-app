/**
 * 
 */
package com.nice.todolist.services;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.nice.todolist.dto.TaskDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.exception.TodoException;
import com.nice.todolist.exception.TodoNotFoundException;
import com.nice.todolist.repositories.TaskRepository;
import com.nice.todolist.services.impl.TaskServiceImpl;
import com.nice.todolist.util.TestUtil;

/**
 * @author nshek
 *
 */
public class TaskServiceTest {

	private static final Long ID = 1L;    
    private static final Date CURRENT_DATE = new Date();
    
    private static final SimpleDateFormat DATEFORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    
    @Mock private TaskRepository mockTaskRepository;
    private TaskService taskService;
    
	@Before
    public void setUp() {
		initMocks(this);
		taskService = new TaskServiceImpl(mockTaskRepository);
	}
	
	@Test
	public void add_NewTask_ShouldBeSavedSuccessfully() {
		TaskDto taskDto = TestUtil.getTestTaskDto();
		
		taskService.createTask(taskDto);
		
		ArgumentCaptor<Task> taskArgument = ArgumentCaptor.forClass(Task.class);
		verify(mockTaskRepository, times(1)).save(taskArgument.capture());
		
		verifyNoMoreInteractions(mockTaskRepository);
		
		Task model = taskArgument.getValue();

        assertNull(model.getId());
        assertThat(model.getName(), is(taskDto.getName()));
        assertThat(model.getDescription(), is(taskDto.getDescription()));
        assertThat(DATEFORMATTER.format(model.getCreatedDate()),is(DATEFORMATTER.format(CURRENT_DATE)));
	}
	
	@Test
    public void findAll_ShouldReturnListOfTasks() {
        List<Task> models = new ArrayList<>();
        when(mockTaskRepository.findAll()).thenReturn(models);

        List<Task> actual = taskService.getAllTasks();

        verify(mockTaskRepository, times(1)).findAll();
        
        verifyNoMoreInteractions(mockTaskRepository);

        assertThat(actual, is(models));
    }
	
	@Test
    public void findById_TaskEntryFound_ShouldReturnFoundTaskEntry()  {
        Task model = TestUtil.getTestTask();model.setId(ID);

        when(mockTaskRepository.fetchByTaskId(ID)).thenReturn(model);

        Task actual = taskService.findById(ID);

        verify(mockTaskRepository, times(1)).fetchByTaskId(ID);
        verifyNoMoreInteractions(mockTaskRepository);

        assertThat(actual, is(model));
    }

	@Test(expected = TodoNotFoundException.class)
    public void findById_TaskEntryNotFound_ShouldThrowException() {
        when(mockTaskRepository.findOne(ID)).thenReturn(null);

        taskService.findById(ID);

        verify(mockTaskRepository, times(1)).findOne(ID);        
        verifyNoMoreInteractions(mockTaskRepository);
    }
	
	@Test
    public void deleteById_TaskEntryFound_ShouldDeleteTaskEntryAndReturnIt() {
        Task model = TestUtil.getTestTask();model.setId(ID);

        when(mockTaskRepository.fetchByTaskId(ID)).thenReturn(model);

        Task actual = taskService.deleteById(ID);

        verify(mockTaskRepository, times(1)).fetchByTaskId(ID);
        verify(mockTaskRepository, times(1)).delete(model.getId());
        
        verifyNoMoreInteractions(mockTaskRepository);

        assertThat(actual, is(model));
    }

	@Test(expected = TodoException.class)
    public void deleteById_TaskEntryFound_ShouldDeleteFailAndThrowException() {
        Task model = TestUtil.getTestTask();model.setId(ID);
         
        when(mockTaskRepository.fetchByTaskId(ID)).thenReturn(model);
        doThrow(new TodoException()).when(mockTaskRepository).delete(model.getId());
        
        taskService.deleteById(ID);

        verify(mockTaskRepository, times(1)).fetchByTaskId(ID);
        verify(mockTaskRepository, times(1)).delete(model.getId());
        
        verifyNoMoreInteractions(mockTaskRepository);
    }
	
    @Test(expected = TodoNotFoundException.class)
    public void deleteById_TaskEntryNotFound_ShouldThrowException() {
        when(mockTaskRepository.findOne(ID)).thenReturn(null);

        taskService.deleteById(ID);

        verify(mockTaskRepository, times(1)).findOne(ID);
        verifyNoMoreInteractions(mockTaskRepository);
    }

    @Test
    public void update_TaskEntryFound_ShouldUpdateSuccessfully() throws TodoNotFoundException {
        TaskDto dto = TestUtil.getUpdatedTestTaskDto();
        
        Task model = TestUtil.getTestTask();model.setId(ID);

        when(mockTaskRepository.fetchByTaskId(dto.getId())).thenReturn(model);
        ArgumentCaptor<Task> taskArgument = ArgumentCaptor.forClass(Task.class);
		

        taskService.updateTask(dto);

        verify(mockTaskRepository, times(1)).fetchByTaskId(dto.getId());
        verify(mockTaskRepository, times(1)).save(taskArgument.capture());
        
        verifyNoMoreInteractions(mockTaskRepository);

        Task actual = taskArgument.getValue();
        
        assertThat(actual.getId(), is(dto.getId()));
        assertThat(actual.getName(), is(dto.getName()));
        assertThat(actual.getDescription(), is(dto.getDescription()));
    }

    @Test(expected = TodoNotFoundException.class)
    public void update_TaskEntryNotFound_ShouldThrowException() throws TodoNotFoundException {
    	TaskDto dto = TestUtil.getUpdatedTestTaskDto();

        when(mockTaskRepository.findOne(dto.getId())).thenReturn(null);

        taskService.updateTask(dto);

        verify(mockTaskRepository, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(mockTaskRepository);
    }
}
