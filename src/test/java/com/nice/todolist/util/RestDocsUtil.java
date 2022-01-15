package com.nice.todolist.util;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

public class RestDocsUtil {
	
	public static RestDocumentationResultHandler documentPrettyPrintReqResp(String useCase) {
        return document(useCase,//"{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()));
    }
}
