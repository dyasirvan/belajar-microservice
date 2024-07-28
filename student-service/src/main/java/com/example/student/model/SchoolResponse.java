package com.example.student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolResponse {

        private Long id;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String createdAt;
        private String updatedAt;
}
