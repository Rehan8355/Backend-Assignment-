package com.example.alumni.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.alumni.http.HttpRequest;
import com.example.alumni.http.HttpServiceEngine;
import com.example.alumni.pojo.AgentStatusResponse;
import com.example.alumni.pojo.AlumniProfile;
import com.example.alumni.pojo.AlumniResponse;
import com.example.alumni.pojo.SearchRequest;
import com.example.alumni.service.helper.CheckAgentStatusHelper;
import com.example.alumni.service.helper.FetchAgentDataHelper;
import com.example.alumni.service.helper.AgentDataJsonHelper;
import com.example.alumni.service.helper.LaunchPhantomAgentHelper;
import com.example.alumni.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlumniServiceImpl implements AlumniService{

	@Value("${launchAgentRequestBody.id}")
	private String agentId;
	
	private final JsonUtils jsonUtils;
	private final HttpServiceEngine httpServiceEngine;
    private final LaunchPhantomAgentHelper launchPhantomAgentHelper;
    private final CheckAgentStatusHelper checkAgentStatusHelper;
    private final FetchAgentDataHelper fetchAgentDataHelper;
    private final AgentDataJsonHelper agentDataJsonHelper;
    
    
   
	@Override
	public ResponseEntity<AlumniResponse> searchAlumniProfile(SearchRequest request) {
		
		//preparing HttpRequest for calling agentlaunch api endpoint and launching the phantom
		HttpRequest launchAgentHttpRequest = launchPhantomAgentHelper.prepareHttpRequestForAgentLaunch(request);
		
		//making the external api call for launching the phantom
		ResponseEntity<String> phantomAgentLaunched = httpServiceEngine.makeHttpCall(launchAgentHttpRequest);
		String responseBody = phantomAgentLaunched.getBody();
		
		//getting the json pasticular field value which containerId
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root=null;;
		try {
			root = mapper.readTree(responseBody);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String containerId = root.get("containerId").asText();
		log.info("container Id"+containerId);
		
		if(phantomAgentLaunched!=null) {
			
			//preparing HttpRequest for calling containersFetch endpoint and checking the phantom status
			HttpRequest agentStatusCheckHttpRequest = checkAgentStatusHelper.prepareHttpRequestForAgentStatus(containerId);
			
			//making the external api call for checking status of  the phantom
			ResponseEntity<String> agentStatusFetched = httpServiceEngine.makeHttpCall(agentStatusCheckHttpRequest);
			String agentStatusJsonBody = agentStatusFetched.getBody();
			
			AgentStatusResponse agentStatusResponse = jsonUtils.fromJson(agentStatusJsonBody, AgentStatusResponse.class);
			if(agentStatusResponse.getStatus().equals("running")) {
				
				try {
			        // Hold the thread
			        Thread.sleep(60000);
			    } catch (InterruptedException e) {
			        Thread.currentThread().interrupt();
			        log.error("Thread was interrupted while waiting for agent to fetch data", e);
			    }
				
				
			}
			
			//preparing HttpRequest for calling containersFetch endpoint and checking the phantom status
			agentStatusCheckHttpRequest = checkAgentStatusHelper.prepareHttpRequestForAgentStatus(containerId);
			
			//making the external api call for checking status of  the phantom
			agentStatusFetched = httpServiceEngine.makeHttpCall(agentStatusCheckHttpRequest);
			agentStatusJsonBody = agentStatusFetched.getBody();
			
		
			 agentStatusResponse = jsonUtils.fromJson(agentStatusJsonBody, AgentStatusResponse.class);
			if(agentStatusResponse.getStatus().equals("finished")) {
				
				//preparing HttpRequest for calling containersfetchoutput endpoint and fetching  the phantom Alumni data
				HttpRequest fetchAgentDataHttpRequest = fetchAgentDataHelper.prepareHttpRequestForFetchAgentData(containerId);
	
				//making the external api call for fetching Alumniprofiles 
				ResponseEntity<String> fetchAgentDataResponse = httpServiceEngine.makeHttpCall(fetchAgentDataHttpRequest);
				String fetchDataResponseBody = fetchAgentDataResponse.getBody();
				log.info("fetched"+fetchDataResponseBody);
				
				//Get the field name from json object  
		        JSONObject obj = new JSONObject(fetchDataResponseBody);
		        String output = obj.getString("output");

		        //Regex to match only .json URL
		        Pattern pattern = Pattern.compile("https?://[^\\s]+\\.json");
		        Matcher matcher = pattern.matcher(output);

		        String jsonUrl = null;
		        if (matcher.find()) {
		            jsonUrl = matcher.group(); // .json URL
		        }
				if (jsonUrl != null) {
				    System.out.println("Extracted JSON URL: " + jsonUrl);
				    
				  //preparing HttpRequest for getting the data present in .json file at aws s3
				    HttpRequest agentJsonDataHttpRequest = agentDataJsonHelper.prepareHttpRequestForAgentDataJson(jsonUrl);
				    
				  //making the external call for fetching actual AlumniProfiles objects in an array 
				    ResponseEntity<String> alumniProfileData = httpServiceEngine.makeHttpCall(agentJsonDataHttpRequest);
				    
		
				    ObjectMapper objectMapper = new ObjectMapper();
				    List<AlumniProfile> alumniProfiles=null;
				    
				    
					try {
						      alumniProfiles = objectMapper.readValue(
						        alumniProfileData.getBody(),
						        new TypeReference<List<AlumniProfile>>() {}
						);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					
					log.info("AlumniResponse is {} ",alumniProfiles);
					
					if(alumniProfiles!=null && !alumniProfiles.isEmpty()) {
					AlumniResponse alumniResponse = new AlumniResponse();
					alumniResponse.setStatus("success");
					alumniResponse.setData(alumniProfiles);
					 return ResponseEntity.ok(alumniResponse);
					
					}else {
						log.warn("alumniprofiles is not present");
					    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
					}
					
					
				} else {
					log.warn("No JSON URL found in response json object");
				    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				}
			}		
			
		}
		
		throw new RuntimeException("agent is not run due to some issue");
		
	}
}