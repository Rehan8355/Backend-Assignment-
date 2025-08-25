package com.example.alumni.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.alumni.pojo.AlumniResponse;
import com.example.alumni.pojo.SearchRequest;
import com.example.alumni.service.AlumniService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/alumni")
@RequiredArgsConstructor
public class AlumniController {

	private final AlumniService alumniService;

	@PostMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<AlumniResponse> searchAlumniProfile(@RequestBody SearchRequest searchRequest) {
		ResponseEntity<AlumniResponse> alumniProfiles = alumniService.searchAlumniProfile(searchRequest);
		return alumniProfiles;
	}

	
}