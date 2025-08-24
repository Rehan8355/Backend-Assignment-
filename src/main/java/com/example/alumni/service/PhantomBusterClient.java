//package com.example.alumni.service;
//
//import com.example.alumni.dto.SearchRequest;
//import com.example.alumni.entity.AlumniProfile;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class PhantomBusterClient {
//
//    private final WebClient webClient = WebClient.builder().build();
//
//    @Value("${phantombuster.apiKey}")
//    private String apiKey;
//
//    @Value("${phantombuster.baseUrl}")
//    private String baseUrl;
//
//    public List<AlumniProfile> searchAlumni(SearchRequest req) {
//        // Build query string for LinkedIn agent
//        String query = String.format("%s %s %s",
//                req.getUniversity(),
//                req.getDesignation(),
//                req.getPassoutYear() == null ? "" : req.getPassoutYear()
//        ).trim();
//
//        // Example payload; adjust for your PhantomBuster agent
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("query", query);
//        payload.put("limit", 50);
//
//        // Replace with your actual agent execution path (needs ?id=AGENT_ID)
//        String path = "https://phantombuster.s3.amazonaws.com/YpTBtOq7yE4/iUIhz9nG2GoduDKdBZBk8Q/result.json";
//
//        // Call PhantomBuster API
//        List<Map<String, Object>> raw = webClient.post()
//                .uri(baseUrl + path)
//                .header("X-Phantombuster-Key-1", apiKey)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(payload)
//                .retrieve()
//                .bodyToMono(List.class)
//                .onErrorResume(ex -> Mono.error(new RuntimeException(
//                        "PhantomBuster call failed: " + ex.getMessage(), ex)))
//                .block();
//        System.out.println(raw);
//
//        // Map results to entities
//        List<AlumniProfile> results = new ArrayList<>();
//        if (raw != null) {
//            for (Map<String, Object> m : raw) {
//                AlumniProfile p = AlumniProfile.builder()
//                        .name((String) m.getOrDefault("name", ""))
//                        .currentRole((String) m.getOrDefault("currentRole", req.getDesignation()))
//                        .university(req.getUniversity())
//                        .location((String) m.getOrDefault("location", ""))
//                        .linkedinHeadline((String) m.getOrDefault("headline", ""))
//                        .passoutYear(req.getPassoutYear())
//                        .linkedinUrl((String) m.getOrDefault("linkedinUrl", ""))
//                        .build();
//                results.add(p);
//            }
//        }
//        return results;
//    }
//}
