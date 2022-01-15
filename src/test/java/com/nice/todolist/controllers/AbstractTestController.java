/**
 * 
 */
package com.nice.todolist.controllers;



import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * @author snyala
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AbstractTestController {

	protected MockMvc mockMvc;

    @Test
    public void contextLoads() {
    }

	protected void setUp(BaseController controller) {
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
								 .setHandlerExceptionResolvers(exceptionResolver())
                                 .setValidator(validator())
                                 .build();
	}

	private HandlerExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
 
        Properties exceptionMappings = new Properties();
 
        exceptionMappings.put("com.nice.todolist.exception.BadRequestException","error/400");
        exceptionMappings.put("org.springframework.web.bind.MethodArgumentNotValidException", "error/400");
        exceptionMappings.put("com.nice.todolist.exception.TodoNotFoundException", "error/404");
        exceptionMappings.put("com.nice.todolist.exception.TodoException", "error/500");
        exceptionMappings.put("java.lang.Exception", "error/error");
        exceptionMappings.put("java.lang.RuntimeException", "error/error");
 
        exceptionResolver.setExceptionMappings(exceptionMappings);
 
        Properties statusCodes = new Properties();
 
        statusCodes.put("error/400", "400");
        statusCodes.put("error/404", "404");
        statusCodes.put("error/500", "500");
        statusCodes.put("error/error", "500");
 
        exceptionResolver.setStatusCodes(statusCodes);
        return exceptionResolver;
    }

    private MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
 
        messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
 
        return messageSource;
    }

    private LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

}
