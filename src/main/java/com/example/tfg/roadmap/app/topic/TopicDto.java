package com.example.tfg.roadmap.app.topic;

import java.util.List;

import com.example.tfg.roadmap.app.resource.ResourceDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicDto {
    private String title;
    private String description;
    private String deadline;
    private List<ResourceDto> resources;
}