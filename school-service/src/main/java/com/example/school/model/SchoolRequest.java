package com.example.school.model;

import com.example.school.validation.constraint.UniqueEmail;
import com.example.school.validation.constraint.UniqueName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolRequest {

    @NotNull
    @UniqueName
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
}
