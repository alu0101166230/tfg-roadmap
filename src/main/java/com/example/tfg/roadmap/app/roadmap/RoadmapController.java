package com.example.tfg.roadmap.app.roadmap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roadmap")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    @PostMapping
    public ResponseEntity<Roadmap> create(@RequestBody RoadmapDto roadmapDto) {
        Roadmap savedRoadmap = roadmapService.createRoadmap(roadmapDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoadmap);
    }

}
