package io.todak.project.myauthservice.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends MockMvcControllerBasement {


    @Test
    public void auth() throws Exception {
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/auth"));

        perform.andExpect(status().isOk());
    }


}