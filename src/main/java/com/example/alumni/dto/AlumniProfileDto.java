package com.example.alumni.dto;

import com.example.alumni.entity.AlumniProfile;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AlumniProfileDto {
    String name;
    String currentRole;
    String university;
    String location;
    String linkedinHeadline;
    Integer passoutYear;
    String linkedinUrl;

    public static AlumniProfileDto from(AlumniProfile e) {
        return AlumniProfileDto.builder()
                .name(e.getName())
                .currentRole(e.getCurrentRole())
                .university(e.getUniversity())
                .location(e.getLocation())
                .linkedinHeadline(e.getLinkedinHeadline())
                .passoutYear(e.getPassoutYear())
                .linkedinUrl(e.getLinkedinUrl())
                .build();
    }
}