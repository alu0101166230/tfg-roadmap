package com.example.tfg.roadmap.app.roadmap;

import java.sql.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tfg.roadmap.app.milestone.Milestone;
import com.example.tfg.roadmap.app.milestone.MilestoneDto;
import com.example.tfg.roadmap.app.milestone.MilestoneRepository;
import com.example.tfg.roadmap.app.resource.Resource;
import com.example.tfg.roadmap.app.resource.ResourceDto;
import com.example.tfg.roadmap.app.resource.ResourceRepository;
import com.example.tfg.roadmap.app.topic.Topic;
import com.example.tfg.roadmap.app.topic.TopicDto;
import com.example.tfg.roadmap.app.topic.TopicRepository;
import com.example.tfg.roadmap.app.user.User;
import com.example.tfg.roadmap.app.user.UserRepository;
import com.example.tfg.roadmap.app.roadmap.RoadmapDto;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;
    private final MilestoneRepository milestoneRepository;
    private final TopicRepository topicRepository; // Assuming TopicRepository exists
    private final ResourceRepository resourceRepository; // Assuming ResourceRepository exists

    public List<Roadmap> getOriginalRoadmaps() {
        return roadmapRepository.findByIsOriginalTrue();
    }
    
    public Roadmap createRoadmap(RoadmapDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Roadmap roadmap = new Roadmap();
        roadmap.setName(dto.getName());
        roadmap.setUser(user);
        roadmap.setOriginal(true); 

        List<Milestone> milestones = dto.getMilestones().stream()
                .map(milestoneDto -> mapMilestone(milestoneDto, roadmap))
                .collect(Collectors.toList());

        List<Milestone> updatedMilestone = updateMilestonesId(milestones);

        updatedMilestone = updatedMilestone.stream()
        .map(m -> {
            m.setId(null);
            return m;
        })
        .collect(Collectors.toList());

        roadmap.setMilestones(updatedMilestone);

        return roadmapRepository.save(roadmap);
    }

    
    private Milestone mapMilestone(MilestoneDto milestoneDto, Roadmap roadmap) {
        Milestone milestone = new Milestone();
        milestone.setId(milestoneDto.getId());
        milestone.setName(milestoneDto.getName());
        milestone.setPreviousNodeId(milestoneDto.getPreviousNodeId());
        milestone.setNextNodeId(milestoneDto.getNextNodeId());
        milestone.setInitial(milestoneDto.isInitial());
        milestone.setFinal(milestoneDto.isFinal());
        milestone.setRoadmap(roadmap);  

        
        List<Topic> topics = milestoneDto.getTopics().stream()
                .map(topicDto -> mapTopic(topicDto, milestone))
                .collect(Collectors.toList());

        milestone.setTopics(topics);

        
        
        return milestone;
    }

    private Topic mapTopic(TopicDto topicDto, Milestone milestone) {
        Topic topic = new Topic();
        topic.setTitle(topicDto.getTitle());
        topic.setDescription(topicDto.getDescription());
        //topic.setDeadline(Date.valueOf(topicDto.getDeadline()));
        topic.setMilestone(milestone);

        
        List<Resource> resources = topicDto.getResources().stream()
                .map(resourceDto -> mapResource(resourceDto, topic))
                .collect(Collectors.toList());

        topic.setResources(resources);
        
        return topic;
    }

    // Helper method to map ResourceDto to Resource entity (do not map the ID)
    private Resource mapResource(ResourceDto resourceDto, Topic topic) {
        Resource resource = new Resource();
        resource.setTitle(resourceDto.getTitle());
        resource.setType(resourceDto.getType());
        resource.setLink(resourceDto.getLink());
        resource.setTopic(topic);
        
        return resource;
    }

    public Roadmap cloneRoadmap (Long roadmapId, Long userId) {
        Optional<Roadmap> roadmapTobeClone = roadmapRepository.findById(roadmapId);

        if (roadmapTobeClone.isPresent()) {

            Roadmap clone = roadmapTobeClone.get();
            User user = userRepository.findById(userId).get();
            Roadmap newRoadmap = new Roadmap();
            newRoadmap.setName(clone.getName());
            newRoadmap.setOriginal(false);
            newRoadmap.setUser(user);

            List<Milestone> clonedMilestones =  clone.getMilestones().stream().map(originalMilestone -> {
                Milestone milestoneClone = new Milestone();
                milestoneClone.setId(originalMilestone.getId());
                milestoneClone.setName(originalMilestone.getName());
                milestoneClone.setInitial(originalMilestone.isInitial());
                milestoneClone.setFinal(originalMilestone.isFinal());
                milestoneClone.setPreviousNodeId(originalMilestone.getPreviousNodeId());
                milestoneClone.setNextNodeId(originalMilestone.getNextNodeId()); //CAN BE NULL
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
            clonedMilestones = updateMilestonesId(clonedMilestones);
            newRoadmap.setMilestones(clonedMilestones);
            return this.roadmapRepository.save(newRoadmap);
        }  
        return new Roadmap();
    } 

    private List<Milestone> updateMilestonesId(List<Milestone> milestones) {
        Optional<Milestone> lastMilestone = milestoneRepository.findTopByOrderByIdDesc();
        final Long lastId = lastMilestone.map(Milestone::getId).orElse(null) + 1;
        //final Integer idToAssing = lastId.intValue() + 1;

        final Integer initialMilestoneId  = milestones.stream()
        .filter(milestone -> milestone.isInitial())
        .findFirst()
        .get()
        .getId()
        .intValue();

        Integer addFactor = lastId.intValue() - initialMilestoneId;

        return milestones.stream().map(milestone -> {
            if (milestone.getPreviousNodeId() != null) {
                milestone.setPreviousNodeId(addFactor + milestone.getPreviousNodeId());
            }
            if (milestone.getNextNodeId() != null) {
                milestone.setNextNodeId(calcualteNewNextNodeId(milestone.getNextNodeId(), addFactor));
            }
            milestone.setId(null);
            return milestone;
        }).toList();
    }

    public String calcualteNewNextNodeId(String input, Integer increment) {
        String[] parts = input.split(",");
        StringBuilder result = new StringBuilder();
    
        for (int i = 0; i < parts.length; i++) {
            long updatedValue = Long.parseLong(parts[i].trim()) + increment;
            result.append(updatedValue);
            if (i < parts.length - 1) {
                result.append(",");
            }
        }
        return result.toString();
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

    public Roadmap getRoadmap(Long id) {
       Optional<Roadmap> roadmap = this.roadmapRepository.findById(id);

       if (roadmap.isPresent()) {
            return roadmap.get();
       } 
       return new Roadmap();
    }



}
