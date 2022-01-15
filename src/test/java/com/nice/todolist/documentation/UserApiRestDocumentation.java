/**
 * 
 */
package com.nice.todolist.documentation;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.context.WebApplicationContext;

import com.nice.todolist.TodoListAppApplication;
import com.nice.todolist.dto.UserDto;
import com.nice.todolist.entities.User;
import com.nice.todolist.repositories.UserRepository;
import com.nice.todolist.util.ConstrainedFields;
import com.nice.todolist.util.RestDocsUtil;
import com.nice.todolist.util.TestUtil;

/**
 * @author snyala
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest//(classes=TodoListAppApplication.class)
@Transactional
public class UserApiRestDocumentation {

	@Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
    private UserRepository userRepository;
	
	private MockMvc mockMvc;
	
	private RestDocumentationResultHandler document;
	/**
	 * Set up MockMVC
	 */
	@Before
	public void setUp() {
		//this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
		FileSystemUtils.deleteRecursively(new File("target/generated-snippets"));
		
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
	                                  .apply(documentationConfiguration(this.restDocumentation))
	                                  .build();
	}
	
	@After
	public void clearOutputDirSystemProperty() {
		System.clearProperty("org.springframework.restdocs.outputDir");
	}
	
	/**
     * All documentation production happens here.
     * 
     * @throws Exception 
     */
    @Test
    public void documentUsersResource() throws Exception {
    	UserDto dto1 = TestUtil.getTestUserDto();
    	dto1.setLastName("Nyala");dto1.setMiddleName("A");dto1.setRoleId("1");//dto1.setCreatedDate(new Date());
    	
    	UserDto dto2 = TestUtil.getTestUserDto();dto2.setEmail("david@nice.com");dto2.setUserName("daviduser");
    	dto2.setLastName("McKinney");dto2.setMiddleName("M");dto2.setRoleId("2");//dto2.setCreatedDate(new Date());
		
        createUser(dto1); createUser(dto2);
               
        getUser(1L);

        listUsers();

        UserDto updatedUserDto = TestUtil.getUpdatedTestUserDto();updatedUserDto.setRoleId("U2");
        updatedUserDto.setLastName("updatedLastName");updatedUserDto.setMiddleName("updtedMiddleName");
        
        updateUser(updatedUserDto);
        
        deleteUser(1L);
        
    }
    
	//@Test
	public void listUsers() throws Exception{		
		
		document = RestDocsUtil.documentPrettyPrintReqResp("list-users")
				               .document(responseFields(userFields(true)));
				
		this.mockMvc.perform(get("/api/users")
				              .contentType(MediaType.APPLICATION_JSON)
				              .accept(MediaType.APPLICATION_JSON)
           ).andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
		    .andExpect(jsonPath("$", hasSize(2)))
	        .andExpect(jsonPath("[*].id").isNotEmpty())
	        .andExpect(jsonPath("[*].userName").isNotEmpty())
	        .andExpect(jsonPath("[*].firstName").isNotEmpty())
	        .andExpect(jsonPath("[*].middleName").isNotEmpty())
	        .andExpect(jsonPath("[*].lastName").isNotEmpty())
	        .andExpect(jsonPath("[*].email").isNotEmpty())
		    .andDo(document);/*document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
				    responseFields(
				     	 fieldWithPath("[].id").description("The users' ID"),
				     	 fieldWithPath("[].userName").description("The users' user name"),
				     	 fieldWithPath("[].firstName").description("The users' first name"),
				     	 fieldWithPath("[].middleName").description("The users' middle name"),
				     	 fieldWithPath("[].lastName").description("The users' last name"),
				     	 fieldWithPath("[].email").description("The users' email address"),
				     	 fieldWithPath("[].roleId").description("The users' role ID"),
				     	 fieldWithPath("[].createdDate").description("The users' created date"),
				     	 fieldWithPath("[].modifiedDate").description("The users' modified date"))));*/
	}
	
	//@Test
	public void getUser(Long userId) throws Exception{
		
		document = RestDocsUtil.documentPrettyPrintReqResp("get-user")
		                       .document(pathParameters(userPathParams()),
		                    		     responseFields(userFields(false)));
		
		mockMvc.perform(get("/api/users/{id}", userId.intValue())
			        	  .contentType(TestUtil.APPLICATION_JSON_UTF8)
			              .accept(TestUtil.APPLICATION_JSON_UTF8)
              ).andExpect(status().isOk())
		       .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		       .andExpect(jsonPath("$.id", is(userId.intValue())))
		       .andDo(document);/*document("{method-name}",
		    		            preprocessRequest(prettyPrint()),
		    		            preprocessResponse(prettyPrint()),
							    responseFields(
							     	 fieldWithPath("id").description("The users' ID"),
							     	 fieldWithPath("userName").description("The users' user name"),
							     	 fieldWithPath("firstName").description("The users' first name"),
							     	 fieldWithPath("middleName").description("The users' middle name"),
							     	 fieldWithPath("lastName").description("The users' last name"),
							     	 fieldWithPath("email").description("The users' email address"),
							     	 fieldWithPath("roleId").description("The users' role ID"),
							     	 fieldWithPath("createdDate").description("The users' created date"),
							     	 fieldWithPath("modifiedDate").description("The users' modified date"))));*/
	}

	//@Test
	public void createUser(UserDto dto) throws Exception{
				
		document = RestDocsUtil.documentPrettyPrintReqResp("create-user")
		                       .document(requestFields(userFieldsWithConstraints()),
				                         responseFields(userFields(false)));
		
		mockMvc.perform(post("/api/users")
		                .contentType(TestUtil.APPLICATION_JSON_UTF8)
		                .accept(TestUtil.APPLICATION_JSON_UTF8)
		                .content(TestUtil.convertObjectToJsonBytes(dto))
		      ).andExpect(status().isCreated())
		       .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		       .andDo(document);/*document("{method-name}",
			   		            preprocessRequest(prettyPrint()),
			   		            preprocessResponse(prettyPrint()),
			   		            requestFields(
			                         fields.withPath("userName").description("The user's user name"),
			                         fields.withPath("firstName").description("The user's first name"),
			                         fields.withPath("lastName").description("The user's last name"),
			                         fields.withPath("email").description("The user's email address"),
			                         fields.withPath("roleId").description("The user's role id"),
			                         fields.withPath("createdDate").description("The user's created date")
	   		            		)
	   		          ));*/
	}
	
	/**
     * Update user 
     * PATCH /users/{id}
     */
	
	//@Test
	public void updateUser(UserDto dto) throws Exception{
		
		document = RestDocsUtil.documentPrettyPrintReqResp("update-user")
		                       .document(pathParameters(userPathParams()),
				                         requestFields(userFieldsWithConstraintsForUpdate()),
				                         responseFields(userFields(false)));
		
		mockMvc.perform(patch("/api/users/{id}",dto.getId().intValue())
			                .contentType(TestUtil.APPLICATION_JSON_UTF8)
			                .accept(TestUtil.APPLICATION_JSON_UTF8)
			                .content(TestUtil.convertObjectToJsonBytes(dto))
               ).andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andDo(document);
	}
	
	/**
     * Delete user 
     * DELETE /users/{userId}
     */
	//@Test
    public void deleteUser(Long userId) throws Exception {
		document = RestDocsUtil.documentPrettyPrintReqResp("delete-user")
		                       .document(pathParameters(userPathParams()),
				                         responseFields(userFields(false)));
		
		mockMvc.perform(delete("/api/users/{id}", userId)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
               ).andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.userName").isNotEmpty())
                .andExpect(jsonPath("$.firstName").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
		        .andDo(document);
    }
	
	private static ParameterDescriptor[] userPathParams() {
        return new ParameterDescriptor[] {
            parameterWithName("id").description(USERS_ID_DESCRIPTION)
        };
    }
	
	private static FieldDescriptor[] userFields(boolean isJsonArray) {
		//ConstrainedFields fields = new ConstrainedFields(UserDto.class);
        return isJsonArray ? 
                new FieldDescriptor[]{
                    fieldWithPath("[]").description("User's list"),
                    fieldWithPath("[].id").description(USERS_ID_DESCRIPTION),
                    fieldWithPath("[].firstName").description(USERS_FIRST_NAME_DESCRIPTION),
                    fieldWithPath("[].middleName").description(USERS_MIDDLE_NAME_DESCRIPTION),
                    fieldWithPath("[].lastName").description(USERS_LAST_NAME_DESCRIPTION),
                    fieldWithPath("[].userName").description(USERS_USERNAME_DESCRIPTION),
                    fieldWithPath("[].email").description(USERS_EMAIL_DESCRIPTION),
                    fieldWithPath("[].roleId").description(USERS_ROLEID_DESCRIPTION),
                    fieldWithPath("[].createdDate").description(USERS_CREATED_DATE_DESCRIPTION),
                    fieldWithPath("[].modifiedDate").description(USERS_MODIFIED_DATE_DESCRIPTION)
                } : 
                new FieldDescriptor[]{
                	fieldWithPath("id").description(USERS_ID_DESCRIPTION),
                	fieldWithPath("firstName").description(USERS_FIRST_NAME_DESCRIPTION),
                	fieldWithPath("middleName").description(USERS_MIDDLE_NAME_DESCRIPTION),
                	fieldWithPath("lastName").description(USERS_LAST_NAME_DESCRIPTION),
                	fieldWithPath("userName").description(USERS_USERNAME_DESCRIPTION),
                	fieldWithPath("email").description(USERS_EMAIL_DESCRIPTION),
                	fieldWithPath("roleId").description(USERS_ROLEID_DESCRIPTION),
                	fieldWithPath("createdDate").description(USERS_CREATED_DATE_DESCRIPTION),
                	fieldWithPath("modifiedDate").description(USERS_MODIFIED_DATE_DESCRIPTION)
                };
    }
	
	private static FieldDescriptor[] userFieldsWithConstraints() {
		ConstrainedFields fields = new ConstrainedFields(UserDto.class);
        return new FieldDescriptor[]{
                    fields.withPath("firstName").description(USERS_FIRST_NAME_DESCRIPTION),
                    fields.withPath("middleName").description(USERS_MIDDLE_NAME_DESCRIPTION),
                    fields.withPath("lastName").description(USERS_LAST_NAME_DESCRIPTION),
                    fields.withPath("userName").description(USERS_USERNAME_DESCRIPTION),
                    fields.withPath("email").description(USERS_EMAIL_DESCRIPTION),
                    fields.withPath("roleId").description(USERS_ROLEID_DESCRIPTION)/*,
                    fields.withPath("createdDate").description(USERS_CREATED_DATE_DESCRIPTION),
                    fields.withPath("modifiedDate").description(USERS_MODIFIED_DATE_DESCRIPTION)*/
               };
    }
	
	private static FieldDescriptor[] userFieldsWithConstraintsForUpdate() {
		ConstrainedFields fields = new ConstrainedFields(UserDto.class);
        return new FieldDescriptor[]{
                	fields.withPath("id").description(USERS_ID_DESCRIPTION),
                    fields.withPath("firstName").description(USERS_FIRST_NAME_DESCRIPTION),
                    fields.withPath("middleName").description(USERS_MIDDLE_NAME_DESCRIPTION),
                    fields.withPath("lastName").description(USERS_LAST_NAME_DESCRIPTION),
                    fields.withPath("userName").description(USERS_USERNAME_DESCRIPTION),
                    fields.withPath("email").description(USERS_EMAIL_DESCRIPTION),
                    fields.withPath("roleId").description(USERS_ROLEID_DESCRIPTION)/*,
                    fields.withPath("createdDate").description(USERS_CREATED_DATE_DESCRIPTION),
                    fields.withPath("modifiedDate").description(USERS_MODIFIED_DATE_DESCRIPTION)*/
               };
    }
	
	private User createUser(String userName, String firstName, String middleName,String lastName, String email,String roleId) {
		User user = new User();	
		user.setUserName(userName);
		user.setFirstName(firstName);
		user.setMiddleName(middleName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setRoleId(roleId);
		user.setCreatedDate(new Date());
		user.setCreatedDate(new Date());
		return userRepository.save(user);	
	}
	
	/**
     * Fields descriptions
     */
    private static final String USERS_USERNAME_DESCRIPTION = "The User's user name";
    private static final String USERS_LAST_NAME_DESCRIPTION = "The User's last name";
    private static final String USERS_FIRST_NAME_DESCRIPTION = "The User's first name";
    private static final String USERS_MIDDLE_NAME_DESCRIPTION = "The User's middle name";
    private static final String USERS_ID_DESCRIPTION = "The User's identifier";
    private static final String USERS_EMAIL_DESCRIPTION = "The User's email address";
    private static final String USERS_CREATED_DATE_DESCRIPTION = "The User's created date";
    private static final String USERS_MODIFIED_DATE_DESCRIPTION = "The User's modified date";
    private static final String USERS_ROLEID_DESCRIPTION = "The User's role id description";
}
