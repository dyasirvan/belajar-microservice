package com.example.student.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassResponse {

    private Long id;
    private String name;
    private SchoolResponse school;
    private List<StudentResponse> students;
}
