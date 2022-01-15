package com.nice.todolist.config;

import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.nice.todolist.services.TaskService;
import com.nice.todolist.services.UserService;

@Configuration
public class TestContext {
	private static final String MESSAGE_SOURCE_BASE_NAME = "i18n/messages";

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasename(MESSAGE_SOURCE_BASE_NAME);
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }

    /*@Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }*/
    
    /*@Bean
    public TaskService taskService() {
        return Mockito.mock(TaskService.class);
    }*/
    
    @Bean
    public TaskService taskService() {
        return Mockito.mock(TaskService.class);
    }
}
