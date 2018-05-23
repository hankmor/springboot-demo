package com.belonk.test.controller;

import com.belonk.controller.DemoController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoControllerTests {

    private MockMvc mvc;

    @Before
    public void setUp() {
		this.mvc = MockMvcBuilders.standaloneSetup(new DemoController()).build();
    }

    @Test
    public void testHello() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.post("/hello").accept(MediaType.TEXT_PLAIN))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Hello World!"));
    }
}
