package com.example.alumni.service;

import com.example.alumni.dto.SearchRequest;
import com.example.alumni.entity.AlumniProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RehanApiCall {


    // Create RestClient instance
    RestClient client = RestClient.create();

    // API endpoint
    String url = "https://api.phantombuster.com/api/v2/agents/launch";
    // API endpoint
    String urlStatus = "https://api.phantombuster.com/api/v2/containers/fetch?id=5452166822073301";
    //API fetch
    String urlFetch="https://api.phantombuster.com/api/v2/containers/fetch-output?id=5452166822073301&mode=json";
    //JsonFetch
    String urlJson="https://phantombuster.s3.amazonaws.com/YpTBtOq7yE4/iUIhz9nG2GoduDKdBZBk8Q/result.json";
    public List<AlumniProfile> searchAlumniApi(SearchRequest req) {
        // Prepare request body (same as your Postman body)
        String requestBody = """
                {
                    "id": "1586821445019611",
                    "argument": {
                      "sessionCookie": "",
                      "search": "VIT Software Engineer",
                       "numberOfResults": 10
                  
                    }
                  }
                  
	        """;

        try {
            // Make POST request
            String response = client.post()
                    .uri(url)
                    .header("X-Phantombuster-Key-1", "")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            System.out.println("API Response:\n" + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//status check

    public List<AlumniProfile> searchAlumniStatus(SearchRequest req) {


        try {
            // Make POST request
            String response = client.get()
                    .uri(urlStatus)
                    .header("X-Phantombuster-Key-1", "")
                   // .contentType(MediaType.APPLICATION_JSON)
                   // .body(requestBody)
                    .retrieve()
                    .body(String.class);

            System.out.println("API Response:\n" + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //fetch

    public List<AlumniProfile> searchAlumniFetch(SearchRequest req) {


        try {
            // Make POST request
            String response = client.get()
                    .uri(urlFetch)
                    .header("X-Phantombuster-Key-1", "")
                    // .contentType(MediaType.APPLICATION_JSON)
                    // .body(requestBody)
                    .retrieve()
                    .body(String.class);

            System.out.println("API Response:\n" + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //json fetch data
    public String searchAlumniJSONFetch(SearchRequest req) {


        String response = null;
        try {
            // Make POST request
            response = client.get()
                    .uri(urlJson)
                    //   .header("X-Phantombuster-Key-1", "")
                    // .contentType(MediaType.APPLICATION_JSON)
                    // .body(requestBody)
                    .retrieve()
                    .body(String.class);

            System.out.println("API Response:\n" + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }





}
