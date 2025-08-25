package com.example.alumni.service;

import org.springframework.http.ResponseEntity;

import com.example.alumni.pojo.AlumniResponse;
import com.example.alumni.pojo.SearchRequest;

public interface AlumniService {

	public ResponseEntity<AlumniResponse> searchAlumniProfile(SearchRequest request);

}
