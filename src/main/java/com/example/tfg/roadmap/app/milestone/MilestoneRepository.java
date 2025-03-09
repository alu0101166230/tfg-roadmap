package com.example.tfg.roadmap.app.milestone;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tfg.roadmap.app.roadmap.Roadmap;

public interface MilestoneRepository  extends JpaRepository<Milestone, Long> {
    Optional<Milestone> findTopByOrderByIdDesc();

}
