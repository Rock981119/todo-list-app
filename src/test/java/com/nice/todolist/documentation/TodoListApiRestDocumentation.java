package com.nice.todolist.documentation;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.collection.IsArray;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.context.WebApplicationContext;

import com.nice.todolist.dto.TodoListDto;
import com.nice.todolist.dto.UserDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.util.ConstrainedFields;
import com.nice.todolist.util.RestDocsUtil;
import com.nice.todolist.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TodoListApiRestDocumentation {

	@Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	private RestDocumentationResultHandler document;
	
	@Before
	public void setUp() {
		FileSystemUtils.deleteRecursively(new File("target/generated-snippets"));		
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
	                                  .apply(documentationConfiguration(this.restDocumentation))
	                                  .build();
	}
	
	/**
     * All documentation production happens here.
     * 
     * @throws Exception 
     */
    @Test
    public void documentTodoListApi() throws Exception {
    	TodoListDto todoListDto = new TodoListDto();
    	todoListDto.setName("first TodoList");todoListDto.setPurpose("for Analsys");
    	Set<Task> tasks = new HashSet<>();
    	Task task1 = new Task();task1.setName("analysis task#1");task1.setDescription("analysis task description");
    	tasks.add(task1);todoListDto.setTasks(tasks);
    	
        createTodoList(todoListDto);
    }

	private void createTodoList(TodoListDto todoListDto) throws IOException, Exception {
		document = RestDocsUtil.documentPrettyPrintReqResp("create-todolist")
                               .document(requestFields(userFieldsWithConstraints()),
	                                     responseFields(userFields()));
		
		mockMvc.perform(post("/api/todoLists")
		                 .contentType(TestUtil.APPLICATION_JSON_UTF8)
		                 .accept(TestUtil.APPLICATION_JSON_UTF8)
		                 .content(TestUtil.convertObjectToJsonBytes(todoListDto))
		      ).andExpect(status().isCreated())
		       .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		       .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.name",is("first TodoList")))
               .andExpect(jsonPath("$.purpose",is("for Analsys")))
               .andExpect(jsonPath("$.tasks").isArray());
		       //.andExpect(jsonPath("$.tasks[*]").isArray());
		       //.andDo(document);
	}
	
	private static FieldDescriptor[] userFieldsWithConstraints() {
		//ConstrainedFields fields = new ConstrainedFields(UserDto.class);
        return new FieldDescriptor[]{
	        		fieldWithPath("name").description("todo List Name"),
	        		fieldWithPath("purpose").description("purpose")
               };
    }
	
	private static FieldDescriptor[] userFields() {
        return new FieldDescriptor[]{
                    fieldWithPath("[]").description("todo list"),
                    fieldWithPath("[].id").description("todo id"),
                    fieldWithPath("[].name").description("todo name"),
                    fieldWithPath("[].purpose").description("todo purpose")
        };
	}
}
