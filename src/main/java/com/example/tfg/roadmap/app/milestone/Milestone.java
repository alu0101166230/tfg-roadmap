package com.example.tfg.roadmap.app.milestone;

import java.util.List;
import com.example.tfg.roadmap.app.roadmap.Roadmap;
import com.example.tfg.roadmap.app.topic.Topic;
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
@Table(name = "milestones")
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Integer previousNodeId;
    private String nextNodeId;
    private boolean isInitial;
    private boolean isFinal;

    @ManyToOne
    @JoinColumn(name = "roadmap_id", nullable = false)
    @JsonBackReference(value = "roadmap-milestones")
    private Roadmap roadmap;

    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "milestone-topics")
    private List<Topic> topics;
}
