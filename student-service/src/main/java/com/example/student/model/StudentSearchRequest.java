package com.example.student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSearchRequest extends SearchPageRequest{
    private String name;
    private String email;
    private String phone;
    private String address;
}
