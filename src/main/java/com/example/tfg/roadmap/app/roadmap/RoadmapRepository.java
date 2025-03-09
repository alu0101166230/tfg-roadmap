package com.example.tfg.roadmap.app.roadmap;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    public List<Roadmap> findByIsOriginalTrue();
}
