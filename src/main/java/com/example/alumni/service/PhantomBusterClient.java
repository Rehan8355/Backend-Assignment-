package com.example.alumni.service;

import com.example.alumni.dto.SearchRequest;
import com.example.alumni.entity.AlumniProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PhantomBusterClient {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${phantombuster.apiKey}")
    private String apiKey;

    @Value("${phantombuster.baseUrl}")
    private String baseUrl;

    /**
     * NOTE: PhantomBuster has different "agents"/"phantoms" for LinkedIn.
     * Adapt the path/body to the specific agent you intend to run (e.g., search, scrape profiles).
     * This method demonstrates a generic call that returns a list of map-like items,
     * later mapped into AlumniProfile entities.
     */
    public List<AlumniProfile> searchAlumni(SearchRequest req) {
        // Build your query string for the LinkedIn agent
        String query = String.format("%s %s %s", req.getUniversity(), req.getDesignation(),
                req.getPassoutYear() == null ? "" : req.getPassoutYear()).trim();

        // Example payload; replace with the exact payload required by your PhantomBuster agent
        Map<String, Object> payload = Map.of(
                "query", query,
                "limit", 50
        );

        // Example path; replace with real PB endpoint for your agent/phantom execution
        String path = "/api/v2/agents/execute"; // placeholder

        // Call PB
        List<Map<String, Object>> raw = webClient.post()
                .uri(baseUrl + path)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(List.class)
                .onErrorResume(ex -> Mono.error(new RuntimeException("PhantomBuster call failed: " + ex.getMessage(), ex)))
                .block();

        // Map raw results to entities (adjust keys per real PB response)
        List<AlumniProfile> results = new ArrayList<>();
        if (raw != null) {
            for (Map<String, Object> m : raw) {
                AlumniProfile p = AlumniProfile.builder()
                        .name((String) m.getOrDefault("name", ""))
                        .currentRole((String) m.getOrDefault("currentRole", req.getDesignation()))
                        .university(req.getUniversity())
                        .location((String) m.getOrDefault("location", ""))
                        .linkedinHeadline((String) m.getOrDefault("headline", ""))
                        .passoutYear(req.getPassoutYear())
                        .linkedinUrl((String) m.getOrDefault("linkedinUrl", ""))
                        .build();
                results.add(p);
            }
        }
        return results;
    }
}