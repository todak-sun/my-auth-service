package io.todak.project.myauthservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
abstract class MockMvcControllerBasement {


    @Autowired
    protected ObjectMapper objectMapper;
    protected MockMvc mvc;


    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentationContextProvider) {
        //TODO : 우아한 형제들 블로그 보고 수정할 것...
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .alwaysDo(
                        document("{method-name}/{step}/",
                                preprocessResponse(prettyPrint())
                        )
                )
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    protected ResultActions assertWithErrorDefaultTemplate(ResultActions actions) throws Exception {
        return actions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionTime").exists())
                .andExpect(jsonPath("$.transactionTime").isString());
    }

    protected ResultActions assertWithErrorTemplateWithDetailError(ResultActions actions) throws Exception {
        return assertWithErrorDefaultTemplate(actions)
                .andExpect(jsonPath("$.error").exists());
    }

    protected ResultActions assertWithResponseDefaultTemplate(ResultActions actions) throws Exception {
        return actions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionTime").exists())
                .andExpect(jsonPath("$.transactionTime").isString());
    }

    protected ResultActions assertWithResponseTemplateWithContent(ResultActions actions) throws Exception {
        return assertWithResponseDefaultTemplate(actions)
                .andExpect(jsonPath("$.content").exists());
    }

}
