package com.example.alumni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String status; // "success" | "error"
    private T data;        // payload or error details
}