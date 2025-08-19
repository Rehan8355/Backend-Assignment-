package com.example.alumni.controller;

import com.example.alumni.dto.ApiResponse;
import com.example.alumni.dto.AlumniProfileDto;
import com.example.alumni.dto.SearchRequest;
import com.example.alumni.service.AlumniService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlumniController {

    private final AlumniService alumniService;

    @PostMapping("/alumnissearch")
    public ResponseEntity<ApiResponse<List<AlumniProfileDto>>> search(@Valid @RequestBody SearchRequest request) {
        List<AlumniProfileDto> data = alumniService.searchAndSave(request);
        return ResponseEntity.ok(new ApiResponse<>("success", data));
    }

    @GetMapping("/alumni/all")
    public ResponseEntity<ApiResponse<List<AlumniProfileDto>>> getAll() {
        List<AlumniProfileDto> data = alumniService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("success", data));
    }
}