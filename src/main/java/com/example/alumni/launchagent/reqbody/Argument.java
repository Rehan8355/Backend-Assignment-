package com.example.alumni.launchagent.reqbody;

import lombok.Data;

@Data
public class Argument {
	
	private String sessionCookie;
	private String search;
	private int numberOfResults;
	private int maxPages;
	 
	

}
