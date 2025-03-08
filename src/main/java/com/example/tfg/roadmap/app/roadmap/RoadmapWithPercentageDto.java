package com.example.tfg.roadmap.app.roadmap;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoadmapWithPercentageDto {
    private Long id;
    private String name;
    private Integer percentageOfCompletation;
    
}
