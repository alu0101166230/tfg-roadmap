package com.example.tfg.roadmap.app.topic;

import java.sql.Date;
import java.util.List;
import com.example.tfg.roadmap.app.milestone.Milestone;
import com.example.tfg.roadmap.app.resource.Resource;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter
@Setter
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Date deadline;
    private String description;
    

    @ManyToOne
    @JoinColumn(name = "milestone_id", nullable = false)
    @JsonBackReference(value = "milestone-topics")
    private Milestone milestone;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "topic-resources")
    private List<Resource> resources;
}
