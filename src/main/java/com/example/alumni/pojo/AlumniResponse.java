package com.example.alumni.pojo;
import java.util.List;

import lombok.Data;

@Data
public class AlumniResponse {
	
    private String status;
    private List<AlumniProfile> data;

    
}
