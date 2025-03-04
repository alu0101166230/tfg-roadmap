package com.example.tfg.roadmap.app.roadmap;

import java.util.List;

import com.example.tfg.roadmap.app.milestone.MilestoneDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoadmapDto {
    private String name;
    private Long userId;
    private List<MilestoneDto> milestones;
}
