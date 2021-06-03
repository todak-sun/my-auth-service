package io.todak.project.myauthservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
abstract class MockMvcControllerBasement {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;
    protected MockMvc mvc;


    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
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