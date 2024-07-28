package com.example.school.controller;

import com.example.school.entity.School;
import com.example.school.model.PagingResponse;
import com.example.school.model.SchoolRequest;
import com.example.school.model.SchoolResponse;
import com.example.school.repository.SchoolRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SchoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @BeforeEach
    void setUp() {
        schoolRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        schoolRepository.deleteAll();
    }

    @Test
    void searchSuccess() throws Exception {
        for (int i = 0; i < 20; i++) {
            School school = new School();
            school.setName("School " + i);
            school.setEmail("school" + i + "@example.com");
            school.setPhone("08123456789" + i);
            school.setAddress("Address " + i);
            schoolRepository.save(school);
        }

        mockMvc.perform(get("/school-service/schools"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    PagingResponse<SchoolResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertEquals(10, response.getData().size());
                    assertEquals(0, response.getCurrentPage());
                    assertEquals(2, response.getTotalPage());
                    assertEquals(10, response.getSize());
                });
    }

    @Test
    void searchNull() throws Exception{
        mockMvc.perform(get("/school-service/schools"))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    PagingResponse<SchoolResponse> pagingResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertEquals(0, pagingResponse.getData().size());
                    assertEquals(0, pagingResponse.getCurrentPage());
                    assertEquals(0, pagingResponse.getTotalPage());
                    assertEquals(10, pagingResponse.getSize());
                });
    }

    @Test
    void getByIdSuccess() throws Exception {
        School school = new School();
        school.setName("School 1");
        school.setEmail("school1@example.com");
        school.setPhone("081234567891");
        school.setAddress("Address 1");
        schoolRepository.save(school);

        mockMvc.perform(get("/school-service/schools/" + school.getId()))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    SchoolResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), SchoolResponse.class);

                    assertEquals(school.getId(), response.getId());
                    assertEquals(school.getName(), response.getName());
                    assertEquals(school.getEmail(), response.getEmail());
                    assertEquals(school.getPhone(), response.getPhone());
                    assertEquals(school.getAddress(), response.getAddress());
                });

    }

    @Test
    void getByIdNull() throws Exception {
        mockMvc.perform(get("/school-service/schools/1"))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void createSuccess() throws Exception {
        SchoolRequest schoolRequest = new SchoolRequest();
        schoolRequest.setName("School 1");
        schoolRequest.setEmail("school1@example.com");
        schoolRequest.setPhone("081234567891");
        schoolRequest.setAddress("Address 1");

        mockMvc.perform(post("/school-service/schools")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(schoolRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    SchoolResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), SchoolResponse.class);

                    assertNotNull(response.getId());
                    assertEquals(schoolRequest.getName(), response.getName());
                    assertEquals(schoolRequest.getEmail(), response.getEmail());
                    assertEquals(schoolRequest.getPhone(), response.getPhone());
                    assertEquals(schoolRequest.getAddress(), response.getAddress());
                });
    }

    @Test
    void createFailedDuplicateName() throws Exception {
        School school = new School();
        school.setName("School 1");
        school.setEmail("school1@example.com");
        school.setPhone("081234567891");
        school.setAddress("Address 1");
        schoolRepository.save(school);

        SchoolRequest schoolRequest = new SchoolRequest();
        schoolRequest.setName("School 2");
        schoolRequest.setEmail("school1@example.com");
        schoolRequest.setPhone("081234567891");
        schoolRequest.setAddress("Address 2");

        mockMvc.perform(post("/school-service/schools")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(school)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void updateSuccess() throws Exception {
        School school = new School();
        school.setName("School 1");
        school.setEmail("school1@example.com");
        school.setPhone("081234567891");
        school.setAddress("Address 1");
        schoolRepository.save(school);

        SchoolRequest schoolRequest = new SchoolRequest();
        schoolRequest.setName("School 2");
        schoolRequest.setEmail("school2@example.com");
        schoolRequest.setPhone("081234567892");
        schoolRequest.setAddress("Address 2");

        mockMvc.perform(put("/school-service/schools/" + school.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(schoolRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    SchoolResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), SchoolResponse.class);

                    assertEquals(school.getId(), response.getId());
                    assertEquals(schoolRequest.getName(), response.getName());
                    assertEquals(schoolRequest.getEmail(), response.getEmail());
                    assertEquals(schoolRequest.getPhone(), response.getPhone());
                    assertEquals(schoolRequest.getAddress(), response.getAddress());
                });
    }

    @Test
    void updateFailedDuplicateEmail() throws Exception {
        School school = new School();
        school.setName("School 1");
        school.setEmail("school1@example.com");
        school.setPhone("081234567891");
        school.setAddress("Address 1");
        schoolRepository.save(school);

        SchoolRequest schoolRequest = new SchoolRequest();
        schoolRequest.setName("School 2");
        schoolRequest.setEmail("school1@example.com");
        schoolRequest.setPhone("081234567892");
        schoolRequest.setAddress("Address 2");

        mockMvc.perform(put("/school-service/schools/" + school.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(schoolRequest)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void deleteSuccess() throws Exception {
        School school = new School();
        school.setName("School 1");
        school.setEmail("school1@example.com");
        school.setPhone("081234567891");
        school.setAddress("Address 1");
        schoolRepository.save(school);

        mockMvc.perform(delete("/school-service/schools/" + school.getId()))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void deleteFailed() throws Exception {
        mockMvc.perform(delete("/school-service/schools/1"))
                .andExpectAll(
                        status().isNotFound()
                );
    }
}