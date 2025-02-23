package com.example.student.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private ClassResponse classResponse;
}
