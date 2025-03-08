package com.example.tfg.roadmap.app.roadmap;

import java.sql.Date;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.tfg.roadmap.app.milestone.Milestone;
import com.example.tfg.roadmap.app.resource.Resource;
import com.example.tfg.roadmap.app.topic.Topic;
import com.example.tfg.roadmap.app.user.User;
import com.example.tfg.roadmap.app.user.UserRepository;
import com.example.tfg.roadmap.app.roadmap.RoadmapDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;

    public Roadmap createRoadmap(RoadmapDto dto) {
        Roadmap roadmap = new Roadmap();
        roadmap.setName(dto.getName());
        roadmap.setOriginal(true);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        roadmap.setUser(user);

        roadmap.setMilestones(dto.getMilestones().stream().map(milestoneDto -> {
            Milestone milestone = new Milestone();
            milestone.setName(milestoneDto.getName());
            milestone.setPreviousNodeId(milestoneDto.getPreviousNodeId());
            milestone.setNextNodeId(milestoneDto.getNextNodeId() == null? null : milestoneDto.getNextNodeId().toString()); //TODO: me guarda []
            milestone.setInitial(milestoneDto.isInitial());
            milestone.setFinal(milestoneDto.isFinal());
            milestone.setRoadmap(roadmap);

            milestone.setTopics(milestoneDto.getTopics().stream().map(topicDto -> {
                Topic topic = new Topic();
                topic.setTitle(topicDto.getTitle());
                topic.setDeadline(Date.valueOf(topicDto.getDeadline()));
                topic.setMilestone(milestone);

                topic.setResources(topicDto.getResources().stream().map(resourceDto -> {
                    Resource resource = new Resource();
                    resource.setTitle(resourceDto.getTitle());
                    resource.setType(resourceDto.getType());
                    resource.setComplete(false);
                    resource.setTopic(topic);
                    return resource;
                }).collect(Collectors.toList()));

                return topic;
            }).collect(Collectors.toList()));

            return milestone;
        }).collect(Collectors.toList()));

        return roadmapRepository.save(roadmap);
    }
}
