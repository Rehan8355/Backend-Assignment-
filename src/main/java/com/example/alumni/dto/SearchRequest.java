package com.example.alumni.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchRequest {
    @NotBlank(message = "university is required")
    private String university;

    @NotBlank(message = "designation is required")
    private String designation;

    private Integer passoutYear; // optional
}