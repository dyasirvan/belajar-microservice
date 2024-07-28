package com.example.school.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolSearchRequest {
    private String name;
    private String email;
    private String phone;
    private String address;

    @NotNull
    @Builder.Default
    private Integer page = 0;

    @NotNull
    @Builder.Default
    private Integer size = 10;
}
