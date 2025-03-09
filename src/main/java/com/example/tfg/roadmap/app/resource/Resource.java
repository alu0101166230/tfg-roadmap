package com.example.tfg.roadmap.app.resource;

import com.example.tfg.roadmap.app.topic.Topic;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter
@Setter
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type;
    private boolean isComplete;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    @JsonBackReference(value = "topic-resources")
    private Topic topic;
}
