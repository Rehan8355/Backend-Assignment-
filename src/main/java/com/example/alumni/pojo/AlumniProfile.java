package com.example.alumni.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlumniProfile {
	
	@JsonProperty("fullName") 
    private String name;
	
	@JsonProperty("jobTitle") 
    private String currentRole;
	
	@JsonProperty("school") 
    private String university;
	
	@JsonProperty("location") 
    private String location;
	
	@JsonProperty("headline") 
    private String linkedinHeadline;
	
	@JsonProperty("schoolDateRange") 
    private String year;

    
}