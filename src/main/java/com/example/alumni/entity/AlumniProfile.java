package com.example.alumni.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alumni_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlumniProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "\"current_role\"")
    private String currentRole;

    @Column(nullable = false)
    private String university;

    private String location;

    @Column(length = 500)
    private String linkedinHeadline;

    private Integer passoutYear;

    @Column(length = 500)
    private String linkedinUrl;
}