package com.example.student.controller;

import com.example.student.entity.ClassEntity;
import com.example.student.entity.Student;
import com.example.student.model.PagingResponse;
import com.example.student.model.StudentRequest;
import com.example.student.model.StudentResponse;
import com.example.student.repository.ClassRepository;
import com.example.student.repository.StudentRepository;
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
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    private ClassEntity getClassEntity() {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Class 1");
        classEntity = classRepository.save(classEntity);
        return classEntity;
    }

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        classRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        classRepository.deleteAll();
    }

    @Test
    void searchSuccess() throws Exception {
        for (int i = 0; i < 20; i++) {
            Student student = new Student();
            student.setName("Student " + i);
            student.setEmail("student" + i + "@example.com");
            student.setPhone("08123456789" + i);
            student.setAddress("Address " + i);
            student.setClassEntity(getClassEntity());
            studentRepository.save(student);
        }

        mockMvc.perform(get("/student-service/students"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    PagingResponse<StudentResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertEquals(10, response.getData().size());
                    assertEquals(0, response.getCurrentPage());
                    assertEquals(2, response.getTotalPage());
                    assertEquals(10, response.getSize());
                });
    }

    @Test
    void searchNull() throws Exception {
        mockMvc.perform(get("/student-service/students"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    PagingResponse<StudentResponse> pagingResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertEquals(0, pagingResponse.getData().size());
                    assertEquals(0, pagingResponse.getCurrentPage());
                    assertEquals(0, pagingResponse.getTotalPage());
                    assertEquals(10, pagingResponse.getSize());
                });
    }

    @Test
    void getByIdSuccess() throws Exception {
        Student student = new Student();
        student.setName("Student 1");
        student.setEmail("student1@example.com");
        student.setPhone("081234567891");
        student.setAddress("Address 1");
        student.setClassEntity(getClassEntity());

        student = studentRepository.save(student);

        Student finalStudent = student;
        mockMvc.perform(get("/student-service/students/" + student.getId()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    StudentResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), StudentResponse.class);

                    assertEquals(finalStudent.getId(), response.getId());
                    assertEquals(finalStudent.getName(), response.getName());
                    assertEquals(finalStudent.getEmail(), response.getEmail());
                    assertEquals(finalStudent.getPhone(), response.getPhone());
                    assertEquals(finalStudent.getAddress(), response.getAddress());
                    assertEquals(finalStudent.getClassEntity().getId(), response.getClassResponse().getId());
                    assertEquals(finalStudent.getClassEntity().getName(), response.getClassResponse().getName());
                });

    }

    @Test
    void getByIdFailed() throws Exception {
        mockMvc.perform(get("/student-service/students/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveSuccess() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setName("Student 1");
        request.setEmail("student1@example.com");
        request.setPhone("081234567891");
        request.setAddress("Address 1");
        request.setClassId(getClassEntity().getId());

        mockMvc.perform(post("/student-service/students")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    StudentResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), StudentResponse.class);

                    assertNotNull(response.getId());
                    assertEquals(request.getName(), response.getName());
                    assertEquals(request.getEmail(), response.getEmail());
                    assertEquals(request.getPhone(), response.getPhone());
                    assertEquals(request.getAddress(), response.getAddress());
                    assertEquals(request.getClassId(), response.getClassResponse().getId());
                });

    }

    @Test
    void saveFailed() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setName("Student 1");
        request.setEmail("student1");
        request.setPhone("081234567891");
        request.setAddress("Address 1");
        request.setClassId(getClassEntity().getId());

        mockMvc.perform(post("/student-service/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSuccess() throws Exception {
        Student student = new Student();
        student.setName("Student 1");
        student.setEmail("student1@example.com");
        student.setPhone("081234567891");
        student.setAddress("Address 1");
        student.setClassEntity(getClassEntity());

        student = studentRepository.save(student);

        StudentRequest request = new StudentRequest();
        request.setName("Student 2");
        request.setEmail("student2@example.com");
        request.setPhone("081234567891");
        request.setAddress("Address 1");
        request.setClassId(getClassEntity().getId());

        mockMvc.perform(put("/student-service/students/" + student.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateFailed() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setName("Student 2");
        request.setEmail("student2@example.com");
        request.setPhone("081234567891");
        request.setAddress("Address 1");
        request.setClassId(getClassEntity().getId());

        mockMvc.perform(put("/student-service/students/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSuccess() throws Exception {
        Student student = new Student();
        student.setName("Student 1");
        student.setEmail("student1@example.com");
        student.setPhone("081234567891");
        student.setAddress("Address 1");
        student.setClassEntity(getClassEntity());

        student = studentRepository.save(student);

        mockMvc.perform(delete("/student-service/students/" + student.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFailed() throws Exception {
        mockMvc.perform(delete("/student-service/students/1"))
                .andExpect(status().isBadRequest());
    }

}