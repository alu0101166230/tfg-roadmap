package com.example.tfg.roadmap.app.topic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tfg.roadmap.app.resource.Resource;

public interface TopicRepository extends JpaRepository<Resource, Long> {

}
