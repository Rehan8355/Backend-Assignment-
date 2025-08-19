package com.example.alumni.repository;

import com.example.alumni.entity.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumniProfileRepository extends JpaRepository<AlumniProfile, Long> {
}