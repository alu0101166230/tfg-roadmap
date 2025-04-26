package com.example.tfg.roadmap.app.milestone;

import java.util.List;

import com.example.tfg.roadmap.app.topic.TopicDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MilestoneDto {
    private Long id;
    private String name;
    private String previousNodeId;
    private String nextNodeId;
    private boolean isInitial;
    private boolean isFinal;
    private List<TopicDto> topics;
}
