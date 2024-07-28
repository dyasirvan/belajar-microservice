package com.example.student.controller;

import com.example.student.client.SchoolClient;
import com.example.student.entity.ClassEntity;
import com.example.student.model.ClassRequest;
import com.example.student.model.ClassResponse;
import com.example.student.model.PagingResponse;
import com.example.student.model.SchoolResponse;
import com.example.student.repository.ClassRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClassRepository classRepository;

    @MockBean
    private SchoolClient schoolClient;

    private SchoolResponse getSchoolResponse(){
        SchoolResponse schoolResponse = new SchoolResponse();
        schoolResponse.setId(1L);
        schoolResponse.setName("School 1");
        return schoolResponse;
    }

    @BeforeEach
    void setUp() {
        classRepository.deleteAll();

        Mockito.when(schoolClient.getSchoolById(1L)).thenReturn(Mono.just(getSchoolResponse()));
    }

    @AfterEach
    void tearDown() {
        classRepository.deleteAll();
    }

    @Test
    void searchSuccess() throws Exception {
        for (int i = 0; i < 20; i++) {
            ClassEntity classEntity = new ClassEntity();
            classEntity.setName("Class " + i);
            classEntity.setSchoolId(1L);

            classRepository.save(classEntity);
        }

        mockMvc.perform(get("/student-service/classes"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    PagingResponse<ClassResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertEquals(10, response.getData().size());
                    assertEquals(0, response.getCurrentPage());
                    assertEquals(2, response.getTotalPage());
                    assertEquals(10, response.getSize());
                });
    }

    @Test
    void searchNull() throws Exception {
        mockMvc.perform(get("/student-service/classes"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    PagingResponse<ClassResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertEquals(0, response.getData().size());
                    assertEquals(0, response.getCurrentPage());
                    assertEquals(0, response.getTotalPage());
                    assertEquals(0, response.getSize());
                });
    }

    @Test
    void getByIdSuccess() throws Exception {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Class 1");
        classEntity.setSchoolId(1L);
        classEntity = classRepository.save(classEntity);

        ClassResponse classResponse = new ClassResponse();
        classResponse.setId(classEntity.getId());
        classResponse.setName(classEntity.getName());
        classResponse.setSchool(getSchoolResponse());
        classResponse.setStudents(new ArrayList<>());

        mockMvc.perform(get("/student-service/classes/" + classEntity.getId()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    ClassResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ClassResponse.class);
                    assertEquals(classResponse, response);
                });

    }

    @Test
    void getByIdNull() throws Exception {
        mockMvc.perform(get("/student-service/classes/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSuccess() throws Exception {
        ClassRequest classRequest = new ClassRequest();
        classRequest.setName("Class 1");
        classRequest.setSchoolId(1L);

        mockMvc.perform(post("/student-service/classes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(classRequest)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    ClassResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ClassResponse.class);
                    assertNotNull(response.getId());
                    assertEquals(classRequest.getName(), response.getName());
                    assertEquals(classRequest.getSchoolId(), response.getSchool().getId());
                });
    }

    @Test
    void createFailed(){
        assertThrows(Exception.class, () -> {
            ClassRequest classRequest = new ClassRequest();
            classRequest.setName("Class 1");
            classRequest.setSchoolId(2L);

            mockMvc.perform(post("/student-service/classes")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(classRequest)));
        });
    }

    @Test
    void updateSuccess() throws Exception {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Class 1");
        classEntity.setSchoolId(1L);
        classEntity = classRepository.save(classEntity);

        ClassRequest classRequest = new ClassRequest();
        classRequest.setName("Class 2");
        classRequest.setSchoolId(1L);

        ClassEntity finalClassEntity = classEntity;
        mockMvc.perform(put("/student-service/classes/" + classEntity.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(classRequest)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    ClassResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ClassResponse.class);
                    assertEquals(finalClassEntity.getId(), response.getId());
                    assertEquals(classRequest.getName(), response.getName());
                    assertEquals(classRequest.getSchoolId(), response.getSchool().getId());
                });
    }

    @Test
    void updateFailed() throws Exception{

        ClassRequest classRequest = new ClassRequest();
        classRequest.setName("Class 1");
        classRequest.setSchoolId(1L);

        mockMvc.perform(put("/student-service/classes/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(classRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteSuccess(){
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(1L);
        classEntity.setName("Class 1");
        classEntity.setSchoolId(1L);
        classRepository.save(classEntity);

        assertDoesNotThrow(() -> {
            mockMvc.perform(delete("/student-service/classes/1"));
        });
    }

    @Test
    void deleteFailed() throws Exception {

        mockMvc.perform(delete("/student-service/classes/1"))
                .andExpect(status().isBadRequest());

    }

}