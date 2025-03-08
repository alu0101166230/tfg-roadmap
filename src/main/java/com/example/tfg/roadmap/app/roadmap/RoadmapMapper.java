package com.example.tfg.roadmap.app.roadmap;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.tfg.roadmap.app.milestone.Milestone;
import com.example.tfg.roadmap.app.resource.Resource;
import com.example.tfg.roadmap.app.topic.Topic;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class RoadmapMapper {



    
    public RoadmapWithPercentageDto mapToRoadmapWithPercentageDto(Roadmap entity) {
       

        RoadmapWithPercentageDto roadmapWithPercentageDto = RoadmapWithPercentageDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .percentageOfCompletation(calculateRoadmapCompletiotionPercentage(entity.getMilestones()))
        .build();

        return roadmapWithPercentageDto;
    }

private Integer calculateRoadmapCompletiotionPercentage(List<Milestone> milestones) {
    if (milestones == null || milestones.isEmpty()) {
        return 0;
    }

    AtomicInteger totalResource = new AtomicInteger(0);
    AtomicInteger completeResources = new AtomicInteger(0);

    milestones.forEach(milestone -> {
        milestone.getTopics().forEach(topic -> {
            totalResource.addAndGet(topic.getResources().size());
            completeResources.addAndGet((int) topic.getResources().stream().filter(Resource::isComplete).count());
        });
    });

    return totalResource.get() == 0 ? 0 : (int) Math.round((completeResources.get() * 100.0) / totalResource.get());
}


}
