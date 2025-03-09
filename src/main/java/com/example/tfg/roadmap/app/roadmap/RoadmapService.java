package com.example.tfg.roadmap.app.roadmap;

import java.sql.Date;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import com.example.tfg.roadmap.app.milestone.Milestone;
import com.example.tfg.roadmap.app.milestone.MilestoneRepository;
import com.example.tfg.roadmap.app.resource.Resource;
import com.example.tfg.roadmap.app.topic.Topic;
import com.example.tfg.roadmap.app.user.User;
import com.example.tfg.roadmap.app.user.UserRepository;
import com.example.tfg.roadmap.app.roadmap.RoadmapDto;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;
    private final MilestoneRepository milestoneRepository;

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
            milestone.setNextNodeId(milestoneDto.getNextNodeId() == null? 
                null : 
                milestoneDto.getNextNodeId().stream().map(String::valueOf).collect(Collectors.joining(",")));
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

    public Roadmap cloneRoadmap (Long roadmapId, Long userId) {
        Optional<Roadmap> roadmapTobeClone = roadmapRepository.findById(roadmapId);

        if (roadmapTobeClone.isPresent()) {
            Optional<Milestone> lastMilestone = milestoneRepository.findTopByOrderByIdDesc();
            Long lastId = lastMilestone.map(Milestone::getId).orElse(null);

            Roadmap clone = roadmapTobeClone.get();
            User user = userRepository.findById(userId).get();
            Roadmap newRoadmap = new Roadmap();
            newRoadmap.setName(clone.getName());
            newRoadmap.setOriginal(false);
            newRoadmap.setUser(user);

            List<Milestone> clonedMilestones =  clone.getMilestones().stream().map(originalMilestone -> {
                Milestone milestoneClone = new Milestone();
                milestoneClone.setName(originalMilestone.getName());
                milestoneClone.setInitial(originalMilestone.isInitial());
                milestoneClone.setFinal(originalMilestone.isFinal());
                if (!originalMilestone.isInitial()) {
                    milestoneClone.setPreviousNodeId(originalMilestone.getPreviousNodeId() + lastId.intValue());
                }
                if (originalMilestone.getNextNodeId() != null) {
                    milestoneClone.setNextNodeId(calculateNewMilestonesId(lastId, originalMilestone.getNextNodeId()));
                }
                milestoneClone.setRoadmap(newRoadmap); // new roadmap reference

                List<Topic> clonedTopics = originalMilestone.getTopics().stream().map(originalTopic -> {
                    Topic topicClone = new Topic();
                    topicClone.setTitle(originalTopic.getTitle());
                    topicClone.setDeadline(originalTopic.getDeadline());
                    topicClone.setDescription(originalTopic.getDescription());
                    topicClone.setMilestone(milestoneClone); // new milestone reference
        
                    // Deep clone the resources for this topic
                    List<Resource> clonedResources = originalTopic.getResources().stream().map(originalResource -> {
                        Resource resourceClone = new Resource();
                        resourceClone.setTitle(originalResource.getTitle());
                        resourceClone.setType(originalResource.getType());
                        resourceClone.setComplete(originalResource.isComplete());
                        resourceClone.setTopic(topicClone); // new topic reference
                        return resourceClone;
                    }).collect(Collectors.toList());
                    topicClone.setResources(clonedResources);
                    return topicClone;
                }).collect(Collectors.toList());
                milestoneClone.setTopics(clonedTopics);
                return milestoneClone;
            }).collect(Collectors.toList());
            newRoadmap.setMilestones(clonedMilestones);
            return this.roadmapRepository.save(newRoadmap);
        }  
        return new Roadmap();
    } 

    private String calculateNewMilestonesId(Long lastMilestoneID, String nextNodeId) {
        // Split the nextNodeId string by commas
        String[] ids = nextNodeId.split(",");
    
        // Initialize a StringBuilder to store the new IDs
        StringBuilder newIds = new StringBuilder();
    
        // Iterate over the array of ids
        for (int i = 0; i < ids.length; i++) {
            // Parse each id as a number, add the lastMilestoneID, and append it to the StringBuilder
            int newId = Integer.parseInt(ids[i]) + lastMilestoneID.intValue();
            newIds.append(newId);
    
            // Append a comma if it's not the last element
            if (i < ids.length - 1) {
                newIds.append(",");
            }
        }
    
        // Return the new string of IDs
        return newIds.toString();
    }



}
