package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void initData() {
        authorRepository.save(new Author("Sam", "Bam"));
        authorRepository.save(new Author("Dan", "Man"));
        authorRepository.save(new Author("Alex", "Rollex"));
    }

    @Test
    @DisplayName("Должен возвращать автора по id")
    public void shouldReturnAuthorById() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/author/1")
            .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andReturn();
        Assertions.assertEquals(
            "{\"id\":1,\"first_name\":\"Sam\",\"last_name\":\"Bam\"}",
            result.getResponse().getContentAsString()
        );
    }

    @Test
    @DisplayName("Должен обрабатывать ошибку если автор не найден")
    public void shouldHandleNotFoundException() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/author/0")
            .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
            .andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andReturn();
        String correctResponse = "{\"message\":\"Author with id=0 not exists\"}";
        Assertions.assertEquals(correctResponse, result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Должен возвращать список авторов")
    public void shouldReturnAuthorsList() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/author")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String correctResponse = "[" +
            "{\"id\":1,\"first_name\":\"Sam\",\"last_name\":\"Bam\"}," +
            "{\"id\":2,\"first_name\":\"Dan\",\"last_name\":\"Man\"}," +
            "{\"id\":3,\"first_name\":\"Alex\",\"last_name\":\"Rollex\"}"  +
        "]";
        Assertions.assertEquals(
                correctResponse,
                result.getResponse().getContentAsString()
        );
    }
}
