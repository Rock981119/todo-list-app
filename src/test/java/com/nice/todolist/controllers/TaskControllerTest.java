package com.nice.todolist.controllers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nice.todolist.dto.TaskDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.exception.BadRequestException;
import com.nice.todolist.exception.TodoException;
import com.nice.todolist.exception.TodoNotFoundException;
import com.nice.todolist.services.TaskService;
import com.nice.todolist.util.Constants;
import com.nice.todolist.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskControllerTest {
	private MockMvc mockMvc;

	@Autowired @Qualifier(value="taskService") 
    private TaskService taskServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
    	Mockito.reset(taskServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void add_TaskEntryWithTooLongValues_ShouldReturnValidationErrors() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setName(TestUtil.createStringWithLength(Constants.MAX_LENGTH_TASKNAME+1));
        dto.setDescription(TestUtil.createStringWithLength(Constants.MAX_LENGTH_DESCRIPTION+1));
        
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                		"name:length must be between 10 and 255"
                	   ,"description:length must be between 0 and 500"
                	   )));
         
        verifyZeroInteractions(taskServiceMock);
    }
    
    @Test
    public void add_NewTaskEntry_ShouldAddTaskEntryAndReturnAddedEntry() throws Exception {
        TaskDto dto = TestUtil.getTestTaskDto();
        Task  added = TestUtil.getTestTask();
        
        when(taskServiceMock.createTask(any(TaskDto.class))).thenReturn(added);
        
        mockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("taskName")))
                .andExpect(jsonPath("$.description", is("taskDescription")));
         
        ArgumentCaptor<TaskDto> dtoCaptor = ArgumentCaptor.forClass(TaskDto.class);
        verify(taskServiceMock, times(1)).createTask(dtoCaptor.capture());
        verifyNoMoreInteractions(taskServiceMock);
        
        TaskDto dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(),is(1L));
        assertThat(dtoArgument.getName(), is("taskName"));
        assertThat(dtoArgument.getDescription(), is("taskDescription"));
    }
    
    @Test
    public void deleteById_TaskEntryFound_ShouldDeleteTaskEntryAndReturnIt() throws Exception {
        Task deleted = TestUtil.getTestTask();

        when(taskServiceMock.deleteById(1L)).thenReturn(deleted);

        mockMvc.perform(delete("/api/tasks/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("taskName")))
                .andExpect(jsonPath("$.description", is("taskDescription")));

        verify(taskServiceMock, times(1)).deleteById(1L);
        verifyNoMoreInteractions(taskServiceMock);
    }

    @Test
    public void deleteById_TaskIsNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(taskServiceMock.deleteById(3L)).thenThrow(new TodoNotFoundException(""));

        mockMvc.perform(delete("/api/tasks/{id}", 3L)
        		.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        	   )
                .andExpect(status().isNotFound());

        verify(taskServiceMock, times(1)).deleteById(3L);
        verifyNoMoreInteractions(taskServiceMock);
    }

    @Test
    public void findAll_TasksFound_ShouldReturnFoundTaskEntries() throws Exception {
        Task first  = TestUtil.getTestTask();first.setName("taskName1");first.setId(1L);
        Task second = TestUtil.getTestTask();second.setName("taskName2");second.setId(2L);
         
        when(taskServiceMock.getAllTasks()).thenReturn(Arrays.asList(first, second));
        
        mockMvc.perform(get("/api/tasks")
        		  .contentType(TestUtil.APPLICATION_JSON_UTF8)
                  .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("taskName1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("taskName2")));
        
        verify(taskServiceMock, times(1)).getAllTasks();
        verifyNoMoreInteractions(taskServiceMock);
    }

//    @Test
//    public void findById_TaskEntryFound_ShouldReturnFoundTaskEntry() throws Exception {
//        Task found = TestUtil.getTestTask();
//
//        when(taskServiceMock.findById(1L)).thenReturn(found);
//
//        mockMvc.perform(get("/api/tasks/{id}", 1L)
//        		.contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .accept(TestUtil.APPLICATION_JSON_UTF8)
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("taskName")))
//                .andExpect(jsonPath("$.description", is("taskDescription")));
//
//        verify(taskServiceMock, times(1)).findById(1L);
//        verifyNoMoreInteractions(taskServiceMock);
//    }

//    @Test
//    public void findById_TaskEntryNotFound_ShouldReturnHttpStatusCode404() throws Exception {
//    	when(taskServiceMock.findById(1L)).thenThrow(new TodoNotFoundException(""));
//
//        mockMvc.perform(get("/api/tasks/{id}", 1L)
//        		.contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .accept(TestUtil.APPLICATION_JSON_UTF8)
//        )
//                .andExpect(status().isNotFound());
//
//        verify(taskServiceMock, times(1)).findById(1L);
//        verifyNoMoreInteractions(taskServiceMock);
//    }

    @Test
    public void update_TaskNameIsTooLong_ShouldReturnValidationErrorsForTaskName() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setName(TestUtil.createStringWithLength(Constants.MAX_LENGTH_TASKNAME + 1));

        mockMvc.perform(patch("/api/tasks/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                		"name:length must be between 10 and 255"
                )));

        verifyZeroInteractions(taskServiceMock);
    }

    @Test
    public void update_TaskEntryNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        TaskDto dto = TestUtil.getUpdatedTestTaskDto();dto.setId(3L);

        when(taskServiceMock.updateTask(any(TaskDto.class))).thenThrow(new TodoNotFoundException(""));

        mockMvc.perform(patch("/api/tasks/{id}", Long.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isNotFound());

        ArgumentCaptor<TaskDto> dtoCaptor = ArgumentCaptor.forClass(TaskDto.class);
        verify(taskServiceMock, times(1)).updateTask(dtoCaptor.capture());
        verifyNoMoreInteractions(taskServiceMock);

        TaskDto dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(), is(Long.MAX_VALUE));
        assertThat(dtoArgument.getName(), is("name_Updated"));
        assertThat(dtoArgument.getDescription(), is("description_Updated"));
    }

    @Test
    public void update_TaskEntryFound_ShouldUpdateTaskEntryAndReturnIt() throws Exception {
        TaskDto dto = TestUtil.getUpdatedTestTaskDto();
        Task updated = TestUtil.getUpdatedTestTask();

        when(taskServiceMock.updateTask(any(TaskDto.class))).thenReturn(updated);

        mockMvc.perform(patch("/api/tasks/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name_Updated")))
                .andExpect(jsonPath("$.description", is("description_Updated")));

        ArgumentCaptor<TaskDto> dtoCaptor = ArgumentCaptor.forClass(TaskDto.class);
        verify(taskServiceMock, times(1)).updateTask(dtoCaptor.capture());
        verifyNoMoreInteractions(taskServiceMock);

        TaskDto dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(), is(1L));
        assertThat(dtoArgument.getName(), is("name_Updated"));
        assertThat(dtoArgument.getDescription(), is("description_Updated"));
    }
}
