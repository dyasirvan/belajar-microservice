package com.example.student.model;

import com.example.student.validation.constraint.UniqueEmail;
import com.example.student.validation.constraint.UniqueStudentName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {

    @NotNull
    @UniqueStudentName
    private String name;

    @NotNull
    @Email
    @UniqueEmail
    private String email;

    @NotNull
    @Size(min = 10, max = 13)
    private String phone;

    @NotNull
    private String address;

    private Long classId;
}
